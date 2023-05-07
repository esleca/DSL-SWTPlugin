package com.dsl.plugin.commandApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.dsl.exceptions.ValueTypeNotFoundException;
import com.dsl.factories.ValueTypeFactory;
import com.dsl.models.dtos.*;
import com.dsl.models.unittests.UnitTest;
import com.dsl.models.valuetypes.ValueType;
import com.dsl.plugin.commandApp.Models.Message;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    private ServerSocket serverSocket;
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

        serverSocket = new ServerSocket(portNumber);
        JSONParser jsonParser = new JSONParser();
        while(true) {
            Socket clientSocket = serverSocket.accept();

            DataInputStream din=new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dout=new DataOutputStream(clientSocket.getOutputStream());
            Message msg = new Gson().fromJson(din.readUTF(), Message.class);
            LOG.info(String.valueOf(msg));

            try {
                JSONObject request = (JSONObject) jsonParser.parse(msg.getRequest());
                //Handling a request to list unit test at class or package level
                if (msg.getRequestCode().equals("LIST")) {
                    if (msg.getListCode().equals("CLASS")) {
                        LOG.info("EXECUTING : request to list unit test");
                        String packageName = (String) request.get("packageName");
                        String className = (String) request.get("className");

                        ClassTestsRequest lstReq = new ClassTestsRequest(packageName, className);
                        List<UnitTestResponse> classList = dsl.getClassUnitTests(lstReq);
                        dout.writeUTF(new Gson().toJson(classList));
                        dout.flush();

                    } else if (msg.getListCode().equals("PACKAGE")) {
                        LOG.info("EXECUTING : request to list unit test");
                        String packageName = (String) request.get("packageName");

                        PackageTestsRequest lstReq = new PackageTestsRequest(packageName);
                        List<UnitTestResponse> pkgList = dsl.getPackageUnitTests(lstReq);
                        LOG.info("Test number: " + pkgList.size());
                        dout.writeUTF(new Gson().toJson(pkgList));
                        dout.flush();
                    } else if(msg.getListCode().equals("FUNCTION")){
                        LOG.info("EXECUTING : request to list class functions");
                        ClassFunctionsRequest classRequest = new Gson().fromJson(String.valueOf(request), ClassFunctionsRequest.class);
                		List<ClassFunctionsResponse> classFunctions = dsl.getClassFunctions(classRequest);
                        dout.writeUTF(new Gson().toJson(classFunctions).toString());
                        dout.flush();
                    }
                } //Handling unit test creation, edition, or deletion
                else {
                    LOG.info(request.toString());
                    //Extract all the parameter necessary to create a unit test
                    String assertion = (String) request.get("assertion");
                    String classPath = (String) request.get("classPath");
                    String language = (String) request.get("language");
                    String outputPath = (String) request.get("outputPath");
                    String function = (String) request.get("function");
                    String testName = (String) request.get("testName");
                    JSONArray parameters = (JSONArray) request.get("parameters");
                    JSONObject exp = (JSONObject) request.get("expected");
                    ValueType expected = null;
                    //Uses factory to obtain the correct value type
                    if(!exp.get("type").toString().equals("")) {
                        expected = ValueTypeFactory.createValueType(exp.get("type").toString(), exp.get("value").toString());
                    }
                    UnitTestRequest utRequest = new UnitTestRequest(classPath, outputPath, language, function, testName, parameters, expected, assertion);
                    if (msg.getRequestCode().equals("CREATE")) {
                        LOG.info("EXECUTING : request to create unit test");
                        dsl.generateUnitTest(utRequest);
                    } else if (msg.getRequestCode().equals("EDIT")) {
                        LOG.info("EXECUTING : request to edit unit test");
                        dsl.generateUnitTest(utRequest);
                    } else if (msg.getRequestCode().equals("DELETE")) {
                        LOG.info("EXECUTING : request to delete unit test");
                        dsl.removeUnitTest(utRequest);
                    }
                }

            } catch (ParseException e) {
                System.err.println("Error reading the test scenarios config file.");
                e.printStackTrace();
            } catch (ValueTypeNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
