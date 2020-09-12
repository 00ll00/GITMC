package oolloo.gitmc;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.TrackingRefUpdate;

import javax.annotation.Nullable;

public class GitHandler {
    private static Git git;
    private static File repo;
    private static String repoName;
    private static final Map<String,File> reposMap=new HashMap<>();
    private static final Map<String,Ref> refMap=new HashMap<>();

    private static final CmdResponse ERROR_REPO_NOT_EXIST = new CmdResponse("Repository not exist. Use command 'git bind <repo>' to bind a repository.",Styles.ERROR).setValue(0);

    public GitHandler(){
        search();
    }

    public Set<String> getReposKey(){
        return reposMap.keySet();
    }
    public Set<String> getBranches(){
        try {
            List<Ref> list = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            refMap.clear();
            for (Ref ref : list){
                refMap.put(ref.getName().replace("refs/heads/","").replaceFirst("refs/remotes/",""), ref);
            }
            return refMap.keySet();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CmdResponse search(){
        reposMap.clear();
        List<File> repos = FileHelper.findFiles(".",".git",true);
        int i = repos.size();
        CmdResponse response = new CmdResponse().setValue(i);
        if(i>0){
            response.append(i+" local repo found:");
            for (File file:repos){
                reposMap.put(file.getParentFile().getName(),file);
                response.append("\n    ").append(file.getParentFile().getName(),Styles.REPO).append(" :  ").append("\""+file.getPath()+"\"",Styles.PATH);
            }
            return response;
        }else {
            return response.append("No repository in server root dir.", Styles.ERROR);
        }
    }
    public CmdResponse bind(String key){
        try {
            if(reposMap.containsKey(key)) {
                repo = reposMap.get(key);
                git = Git.open(repo);
                repoName = key;
                return new CmdResponse().append("Git bind successful to repo ").append(repoName,Styles.REPO).append(" ("+repo.getPath()+")",Styles.PATH);
            } else {
                return new CmdResponse().append("Unknown repository name '"+key+"', use command 'git search' and try again.").setValue(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse getBind(){
        if (repo != null && repo.exists()){
            return new CmdResponse("Git is binding to ",1).append(repoName,Styles.REPO).append(" ("+repo.getPath()+")", Styles.PATH);
        }else {
            return new CmdResponse("Git is binding to nothing. Use command 'git bind <repo>' to bind a repository.",0);
        }
    }

    public CmdResponse pull() {
        if (repo == null || !repo.exists()) return ERROR_REPO_NOT_EXIST;
        try {
            git=Git.open(repo);
            PullCommand pull = git.pull().setTransportConfigCallback(SshHandler.getTransferConfigCallback());
            PullResult result = pull.call();
            if(result.isSuccessful()){
                return new CmdResponse(result.toString());
            }else {
                return new CmdResponse(result.toString(),0);
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse fetch(){
        if (repo == null || !repo.exists()) return ERROR_REPO_NOT_EXIST;
        try {
            git=Git.open(repo);
            FetchCommand fetch = git.fetch().setTransportConfigCallback(SshHandler.getTransferConfigCallback());
            FetchResult result = fetch.call();
            Collection<TrackingRefUpdate> refUpdates = result.getTrackingRefUpdates();
            CmdResponse response = new CmdResponse();
            if (refUpdates.size()>0){
                response.append("From ").append(result.getURI().toString(),Styles.URL);
                for(TrackingRefUpdate update:refUpdates){
                    response.append("\n    ").append(update.getResult().toString()).append(update.getNewObjectId().getName()).append(update.getRemoteName(),Styles.REPO).append("    ->    ").append(update.getLocalName(),Styles.REPO);
                }
            }else {
                response.append("Nothing need to fetch.");
            }

            return response;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse add(String pattern){
        if (repo == null || !repo.exists()) return ERROR_REPO_NOT_EXIST;
        try {
            git.add().addFilepattern(pattern).call();
            return new CmdResponse("Changes staged.");

        } catch (GitAPIException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse commit(String message){
        if (repo == null || !repo.exists()) return ERROR_REPO_NOT_EXIST;
        try {
            RevCommit revCommit = git.commit().setMessage(message).call();
            return new CmdResponse("commit test");
        } catch (GitAPIException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse status() {
        if (repo == null || !repo.exists()) return ERROR_REPO_NOT_EXIST;
        try {
            Status status = git.status().call();
            Set<String> a = status.getConflicting();
            if (status.isClean()){
                return new CmdResponse().append("Repo ").append(repoName,Styles.REPO).append(" : On branch ").append(git.getRepository().getFullBranch(),Styles.BRANCH).append("\nNothing to commit, working tree clean.");
            }else {
                CmdResponse response = new CmdResponse();
                Set<String> files;
                response.append("Repo ").append(repoName,Styles.REPO).append(" : On branch ").append(git.getRepository().getBranch(),Styles.BRANCH);
                files=status.getChanged();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) changed:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files),10)),Styles.STATUS_CHANGED);
                files=status.getAdded();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) added:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files), 10)),Styles.STATUS_ADDED);
                files=status.getRemoved();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) removed:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files),10)),Styles.STATUS_REMOVED);
                files=status.getModified();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) modified:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files),10)),Styles.STATUS_MODIFIED);
                files=status.getUntracked();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) untracked:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files),10)),Styles.STATUS_UNTRACKED);
                files=status.getMissing();
                if (!files.isEmpty())
                    response.append("\n").append(files.size()).append(" file(s) missing:\n    ");
                    response.append(String.join("\n    ",limit(new ArrayList<>(files),10)),Styles.STATUS_MISSING);
                return response.setValue(0);
            }

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse branch(@Nullable String branch) {
        if (branch==null || Objects.equals(branch, "")) {

            Set<String> branches = getBranches();
            int size = branches.size();
            CmdResponse response = new CmdResponse("Repo ").append(repoName,Styles.REPO).append(" : Branches");
            try {
                for(Ref ref : refMap.values()){
                    String name = ref.getName();
                    if(name.equals("HEAD") || name.endsWith(git.getRepository().getFullBranch())){
                        response.append("\n  * ");
                    }else {
                        response.append("\n    ");
                    }
                    response.append(name.replaceFirst("refs/heads/","").replaceFirst("refs/remotes/",""),Styles.BRANCH).append("    "+ref.getObjectId().getName().substring(0,7)+"...",Styles.ID);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new CmdResponse(e);
            }
            return response.setValue(size);
        }else {
            try {
                Ref ref = git.branchCreate().setName(branch).call();
                return new CmdResponse("Created branch: ").append(ref.getName(),Styles.BRANCH);
            } catch (GitAPIException e) {
                e.printStackTrace();
                return  new CmdResponse(e);
            }
        }
    }
    public CmdResponse checkout(String branch,@Nullable String path){
        CmdResponse response = new CmdResponse();
        CheckoutCommand command = git.checkout().setCreateBranch(false).setName(branch);
        if (path!=null && path!=""){
            if (path.equals("."))
                command.setAllPaths(true);
            command.addPath(path);
        }
        try {
            command.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
        CheckoutResult result = command.getResult();
        response.append("Checkout "+result.getStatus().name());
        List<String> files;
        files=result.getModifiedList();
        if (files!=null && !files.isEmpty()) {
            response.append("\n").append(files.size()).append(" file(s) modified:\n    ");
            response.append(String.join("\n    ", limit(new ArrayList<>(files), 10)), Styles.STATUS_MODIFIED);
        }
        files=result.getRemovedList();
        if (files!=null && !files.isEmpty()) {
            response.append("\n").append(files.size()).append(" file(s) removed:\n    ");
            response.append(String.join("\n    ", limit(new ArrayList<>(files), 10)), Styles.STATUS_REMOVED);
        }
        files=result.getConflictList();
        if (files!=null && !files.isEmpty()) {
            response.append("\n").append(files.size()).append(" file(s) conflicted:\n    ");
            response.append(String.join("\n    ", limit(new ArrayList<>(files), 10)), Styles.STATUS_MODIFIED);
        }
        files=result.getUndeletedList();
        if (files!=null && !files.isEmpty()) {
            response.append("\n").append(files.size()).append(" file(s) undeleted:\n    ");
            response.append(String.join("\n    ", limit(new ArrayList<>(files), 10)), Styles.STATUS_MODIFIED);
        }

        return response;
    }
    public CmdResponse push(){
        try {
            CmdResponse response = new CmdResponse();
            Iterable<PushResult> result = git.push().setTransportConfigCallback(SshHandler.getTransferConfigCallback()).call();
            result.forEach((r)->{
                response.append(r.getMessages()+"\n");
            });
            return response;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    private List<String> limit(List<String> in, int limit){
        if (in.size()>limit){
            List<String> out = in.stream().limit(limit).collect(Collectors.toList());
            out.add("...");
            return out;
        }else {
            return in;
        }
    }
//    private Pattern getPattern(String path){
//        if (path.equals("."))
//            return Pattern.compile("^.*$");
//        path = path.replace(".","\\.");
//        path = path.replace("^","\\^");
//        path = path.replace("$","\\$");
//        path = path.replace("*",".*");
//        path = path.replace("?",".?");
//        return Pattern.compile("^"+path+"$");
//    }
}
