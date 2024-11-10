package oldfathw;

import java.io.File;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Bot extends TelegramLongPollingBot {

    public static Long id;

    @Override
    public String getBotUsername() {
        return "GeosuggesterBot";
    }

    @Override
    public String getBotToken() {
        return "7785825725:AAGTYfLA_WgvZ1BL2jNlmKAGkgNoISFEw9s"; // !!! In the future, you should consider storing your
                                                                 // token in a dedicated settings file or in environment
                                                                 // variables.
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) // Who are we sending a message to
                .text(what).build(); // Message content
        try {
            execute(sm); // Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); // Any error will be printed here
        }
    }

    public void csvRead() throws CsvValidationException {
        String csvFile = "data.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Name: " + line[0] + ", Age: " + line[1] + ", City: " + line[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createXlsx() throws FileNotFoundException, IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Example");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Hello"); // Ячейка A1 со словом "Привет"
            row.createCell(1).setCellValue("World"); // И ячейка B1 с изложением "Мир"

            try (FileOutputStream out = new FileOutputStream("Example.xlsx")) {
                workbook.write(out);
            } // Работа с файлом завершена, он закрыт
            System.out.println(workbook.getPrintArea(0));
        }
    }

    @Override
    public void onUpdateReceived(Update update) { // This is the most important method. It will be called automatically
                                                  // whenever a new Update is available
        Message msg = update.getMessage();
        User user = msg.getFrom();
        id = user.getId();
        String text = msg.getText();
        if (text.equals("/start")) {
            sendText(id, "Random country:");
            sendText(id, readCountryFromTxt());

        } else if (text.equals("Hi")) {
            sendText(id, "Good boy!");
        } else if (text.equals("Расписание")) {
            sendText(id, "Расписание!");
        } else {
            sendText(id, "We need to greate each other ;)");
        }
        // String WhoWhat = user.getFirstName() + " wrote " + msg.getText() + " " +
        // user.getLanguageCode();
        // System.out.printf("\n\n %s\n\n", WhoWhat);

        // System.out.println(user.getFirstName() + " wrote " + msg.getText());
        System.out.println(msg.getText());
    }

    public String readCountryFromTxt() {
        String country = new String();
        country = "USA";
        int countOfCoutnry = countLines();
        Random r = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = r.nextInt((countOfCoutnry - 1) + 1) + 1;
        System.out.println(randomNum);

        return country;
    }

    public int countLines() {
        long result = 0;
        try (   FileReader input = new FileReader("country.txt");
                LineNumberReader count = new LineNumberReader(input);) {
            while (count.skip(Long.MAX_VALUE) > 0) {
                // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read
                // the entire file
            }
            result = count.getLineNumber() + 1; // +1 because line index starts at 0
        }
        catch (Exception e) {
        }
        return (int)result;
    }

}
