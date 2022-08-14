package com.dsl.plugin.commandApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.tec.dslunittests.plugin.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dsl.fachade.IDSLFachade;

@SpringBootApplication
public class CommandAppApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(CommandAppApplication.class);
    private IDSLFachade dsl;

    private static final int portNumber = 3399;

    public CommandAppApplication(IDSLFachade dsl) {
        this.dsl = dsl;
    }

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(CommandAppApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    /**
     * This method will be executed after the application context is loaded and
     * right before the Spring Application main method is completed.
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();

            // create a DataInputStream so we can read data from it.
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            LOG.info("EXECUTING : before message");
            Object obj = objectInputStream.readObject();
            Message msg = (Message) obj;
            if (msg.getRequestCode().equals("CREATE")) {
                LOG.info("EXECUTING : request to create unit test");
                dsl.createUnitTest(msg.getRequest());
            } else if (msg.getRequestCode().equals("EDIT")) {
                LOG.info("EXECUTING : request to edit unit test");
                dsl.editUnitTest(msg.getRequest());
            } else if (msg.getRequestCode().equals("DELETE")) {
                LOG.info("EXECUTING : request to delete unit test");
                dsl.removeUnitTest(msg.getRequest());
            } else if (msg.getRequestCode().equals("LIST")) {
                LOG.info("EXECUTING : request to list unit test");
                dsl.createUnitTest(msg.getRequest());
            }
            serverSocket.close();


        } catch (Exception e) {
            LOG.info("Something went wrong ¯\\_(ツ)_/¯");
            LOG.info(e.getMessage());
            e.printStackTrace();
        }
    }

}
