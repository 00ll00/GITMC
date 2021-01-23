package oolloo.gitmc;

import com.jcraft.jsch.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SshHandler {
    public static String privateKey;
    public static String publicKey;
    public static String fingerPrint;

    private static final File PRIVATE_KEY_FILE = new File(GITMC.SSH_PATH+"id_rsa");
    private static final File PUBLIC_KEY_FILE = new File(GITMC.SSH_PATH+"id_rsa.pub");

    public SshHandler(){
        readSshKey();
    }

    public static TransportConfigCallback getTransferConfigCallback(){
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }
            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch jSch = new JSch();
                JSch.setConfig("ssh-rsa", JSch.getConfig("signature.rsa"));
                configureJSch(jSch);
                jSch.addIdentity(GITMC.SSH_PATH+"id_rsa");
                jSch.setKnownHosts(GITMC.SSH_PATH+"known_hosts");
                return jSch;
            }
        };
        return transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };
    }

    public CmdResponse keyGen(String comment,boolean replace){
        if(keyExist()&&!replace)
            return new CmdResponse("SSH-Key already exist. Use command 'ssh keygen <comment> replace' to change the key anyway.",Styles.ERROR).setValue(0);
        JSch jSch =new JSch();
        try {
            KeyPair keyPair = KeyPair.genKeyPair(jSch,KeyPair.RSA);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            keyPair.writePrivateKey(outputStream);
            privateKey=outputStream.toString();
            outputStream.reset();
            keyPair.writePublicKey(outputStream,comment);
            publicKey=outputStream.toString();
            fingerPrint=keyPair.getFingerPrint();
            writeKey();
            return new CmdResponse().append("New SSH-Key generated.  ").append("[Copy]", Styles.CLICKABLE.func_240715_a_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, publicKey)).func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_,new StringTextComponent("Click to copy the public key"))))
                    .append("\nYour identification has been saved in ").append(PRIVATE_KEY_FILE.getPath(), Styles.PATH)
                    .append("\nYour public key has been saved in ").append(PUBLIC_KEY_FILE.getPath(), Styles.PATH)
                    .append("\nThe key fingerprint is: ").append(fingerPrint,Styles.INFO);
        } catch (JSchException | IOException e) {
            e.printStackTrace();
            return new CmdResponse(e);
        }
    }
    public CmdResponse check(){
            if(keyExist()) {
                try {
                    KeyPair keyPair = KeyPair.load(new JSch(),privateKey.getBytes(),publicKey.getBytes());
                    fingerPrint = keyPair.getFingerPrint();
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                return new CmdResponse().append("SSH-Key exist.  ").append("[Copy]", Styles.CLICKABLE.func_240715_a_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, publicKey)).func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_,new StringTextComponent("Click to copy the public key"))))
                        .append("\nYour identification saved in ").append(PRIVATE_KEY_FILE.getPath(), Styles.PATH)
                        .append("\nYour public key saved in ").append(PUBLIC_KEY_FILE.getPath(), Styles.PATH)
                        .append("\nThe key fingerprint is: ").append(fingerPrint,Styles.INFO);
            }else {
                return new CmdResponse("SSH-Key not exist. Use command 'ssh keygen <comment>' to generate keys.",Styles.ERROR).setValue(0);
            }
    }

    public boolean keyExist(){
        return PRIVATE_KEY_FILE.exists()&&PUBLIC_KEY_FILE.exists();
    }

    private void readSshKey() {
        if(keyExist()){
            try {
                privateKey=FileHelper.readText(PRIVATE_KEY_FILE);
                publicKey=FileHelper.readText(PUBLIC_KEY_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeKey() throws IOException {
        GITMC.CheckFiles();
        FileHelper.writeText(PRIVATE_KEY_FILE,privateKey);
        FileHelper.writeText(PUBLIC_KEY_FILE,publicKey);
    }
}
