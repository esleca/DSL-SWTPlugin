package com.dsl.plugin.commandApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.dsl.exceptions.ValueTypeNotFoundException;
import com.dsl.factories.ValueTypeFactory;
import com.dsl.models.dtos.ClassTestsRequest;
import com.dsl.models.dtos.PackageTestsRequest;
import com.dsl.models.dtos.UnitTestRequest;
import com.dsl.models.unittests.UnitTest;
import com.dsl.models.valuetypes.ValueType;
import com.dsl.plugin.commandApp.Models.Message;
import com.google.gson.Gson;
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
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
        OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());

        Message msg = new Gson().fromJson(input, Message.class);

        JSONParser jsonParser = new JSONParser();

        try  {
            JSONObject request = (JSONObject) jsonParser.parse(msg.getRequest());
            if (msg.getRequestCode().equals("LIST")) {
                if(msg.getListCode().equals("CLASS")){
                    LOG.info("EXECUTING : request to list unit test");
                    String packageName = (String) request.get("packageName");
                    String className = (String) request.get("className");

                    ClassTestsRequest lstReq = new ClassTestsRequest(packageName, className);
                    List<UnitTest> classList = dsl.getClassUnitTests(lstReq);
                    List<String> strLst = new ArrayList<>();
                    classList.forEach( element -> {
                        strLst.add(element.toString());
                    });
                    out.write(new Gson().toJson(strLst).toString());
                    //out.close();
                }
                else if(msg.getListCode().equals("PACKAGE")){
                    LOG.info("EXECUTING : request to list unit test");
                    String packageName = (String) request.get("packageName");

                    PackageTestsRequest lstReq = new PackageTestsRequest(packageName);
                    dsl.getPackageUnitTests(lstReq);
                }
            }
            else {
                String className = (String) request.get("className");
                String assertion = (String) request.get("assertion");
                String classPath = (String) request.get("classPath");
                String language = (String) request.get("language");
                String outputPath = (String) request.get("outputPath");
                String function = (String) request.get("function");
                String testName = (String) request.get("testName");
                JSONArray parameters = (JSONArray) request.get("parameters");
                JSONObject exp = (JSONObject) request.get("expected");
                ValueType expected = ValueTypeFactory.createValueType(exp.get("type").toString(), exp.get("value").toString());
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
       /* try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
            Message msg = new Gson().fromJson(input, Message.class);
            UnitTestRequest request =  new Gson().fromJson(msg.getRequest(), UnitTestRequest.class);
            LOG.info(msg.getRequest());
            if (msg.getRequestCode().equals("CREATE")) {
                LOG.info("EXECUTING : request to create unit test");
                dsl.createUnitTest(request);
            } else if (msg.getRequestCode().equals("EDIT")) {
                LOG.info("EXECUTING : request to edit unit test");
                dsl.editUnitTest(request);
            } else if (msg.getRequestCode().equals("DELETE")) {
                LOG.info("EXECUTING : request to delete unit test");
                dsl.removeUnitTest(request);
            } else if (msg.getRequestCode().equals("LIST")) {
                LOG.info("EXECUTING : request to list unit test");
                dsl.createUnitTest(request);
            }
            serverSocket.close();


        } catch (Exception e) {
            LOG.info("Something went wrong ¯\\_(ツ)_/¯");
            LOG.info(e.getMessage());
            e.printStackTrace();
        }*/
    }

    public UnitTestRequest test() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
        Message msg = new Gson().fromJson(input, Message.class);

        String testScenarioPath = "D:\\TEC\\2022\\I semestre\\Asistencia\\DSL-SWTPlugin\\src\\com\\tec\\dslunittests\\resources\\package.json";

        JSONParser jsonParser = new JSONParser();

        try  {

            JSONObject request = (JSONObject) jsonParser.parse(msg.getRequest());
            String className = (String) request.get("className");
            String assertion = (String) request.get("assertion");
            String classPath = (String) request.get("classPath");
            String language = (String) request.get("language");
            String outputPath = (String) request.get("outputPath");
            String function = (String) request.get("function");
            String testName = (String) request.get("testName");
            JSONArray parameters = (JSONArray) request.get("parameters");
            JSONObject exp = (JSONObject) request.get("expected");
            ValueType expected = ValueTypeFactory.createValueType( exp.get("type").toString(), exp.get("value").toString());


            UnitTestRequest utRequest =  new UnitTestRequest(classPath, outputPath, language, function, testName, parameters, expected, assertion);
            return utRequest;

        } catch (ParseException e) {
            System.err.println("Error reading the test scenarios config file.");
            e.printStackTrace();
        } catch (ValueTypeNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
