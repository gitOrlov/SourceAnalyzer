import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassAnalyzer {

    private static File dirWithFiles = new File("C:\\sert"); // директория с исходниками
    private static byte[] fileContent; //массив байт
    private static byte[] fileContent2; //массив байт

    public static void main(String[] args) throws IOException
    {
        for (File itemFile : dirWithFiles.listFiles())
        {
            if (itemFile.isFile()) setFileContent(itemFile); //если itemFiles является файлом, то используем метод setFileContent
        }
    }

    private static void setFileContent (File targetFile) throws IOException
    {
        FileInputStream openFile = new FileInputStream(targetFile);
        fileContent = new byte [openFile.available()];  // cчитываем содержимое в массив байтов
        openFile.read(fileContent);
        openFile.close();

        System.out.println();
        System.out.println("В файле " + targetFile.getName() + ":");
        searchInContent("class");
    }

    private static void searchInContent(String searchWord) throws IOException
    {
        String stringContent = new String(fileContent, "UTF-8"); //запись содержимого файла в строку

        int numberLine = 0;
        for (String targetLine : stringContent.split("\n"))  //по всем строкам в файле разделенным \n
        {
            String[] wordsInLine = targetLine.split(" "); // определение слова как разделенное через пробел

            for (int i = 0; i < wordsInLine.length; i++)  // по всем словам в строке
            {
                if (wordsInLine[i].equals(searchWord)) // если текущее слово равно "class"
                {
                    String classname = wordsInLine[i+1]; // то имя класса становится следующее слово
                    System.out.println(searchWord + " " + classname + " объявлен в строке " + (numberLine + 1));

                    //поиск найденного класса в файлах
                    int init = 0;
                    for (File itemFiles : dirWithFiles.listFiles())
                    {
                        if (itemFiles.isFile())//если itemFiles является файлом, то
                        {
                            ifFile(itemFiles);

                            String stringContent2 = new String(fileContent2, "UTF-8");
                            int numberLine2 = 0;
                            for (String targetLine2 : stringContent2.split("\n"))
                            {
                                String [] wordsInLine2 = targetLine2.split(" ");
                                for (int j = 0; j < wordsInLine2.length; j++) {

                                    if (classname.equals(wordsInLine2[j]))//если поисковое слово равно i-му слову
                                    {
                                        System.out.println("Класс " + classname + " используется в " + itemFiles.getName() +" файле "+ (numberLine2 + 1) + " строке, "+(j+1)+" слово");
                                        init = init++;
                                    }
                                }
                                numberLine2++;
                            }
                        }
                    }
                    if (init < 2) System.out.println("класс " + classname + " НЕ ИСПОЛЬЗУЕТСЯ! КЛАСС ИЗБЫТОЧЕН!");
                }
            }
            numberLine++;
        }
    }

    private static void ifFile (File file) throws IOException
    {
        FileInputStream openFile2 = new FileInputStream(file);
        fileContent2 = new byte[openFile2.available()];
        openFile2.read(fileContent2);
        openFile2.close();
    }
}
