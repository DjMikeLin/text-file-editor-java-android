package com.c1tech.textfilemanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyActivity extends AppCompatActivity
{
    public EditText ed;
    public TextView result;
    public Button save, load;
    public File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/t1.txt");
    public File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/tempFile.txt");

    /*All necessary variables(identified by id in the Manifest) requiring different actions upon them are initialized
    * by the onCreate method when the app is ran. */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ed = (EditText) findViewById(R.id.editText);
        result = (TextView) findViewById(R.id.textView);
        save = (Button) findViewById(R.id.replace);
        load = (Button) findViewById(R.id.load);
    }

    /*Method clears editText(ed) when called. Also, keyboard is hidden upon call.*/
    public void clear(View view)
    {
        ed.setText("");
        InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /*When user hits the "REPLACE FILE" button, the buttonReplace method is called at which time the user
    * input at editText(ed) is put into a String array called addText. The addToText method is then called
    * with a single parameter addText. Then clear(view) and result is both cleared of any text.*/
    public void buttonReplace (View view){
        String [] addText = String.valueOf(ed.getText()).split(System.getProperty("line.separator"));
        addToText(addText);
        clear(view);
        result.setText("");
    }

    /*Method addToText takes one String[] and overwrites the global variable File file by replacing
    * it with the contents stored in the given array. By using FileWriter writer =
    * new FileWriter(file, false), anything writen(.write) to file will now overwrite the
    * old file.*/
    public void addToText(String[] text)
    {
        FileWriter writer;

        try
        {
            writer = new FileWriter(file, false);
            for (int i = 0; i < text.length; i++)
            {
                writer.write(text[i].toString());

                if (i < text.length - 1)
                {
                    writer.write("\n");
                }
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*When user hits the "Load" button, the buttonLoad method is called at which time the contents
    of File file global variable will be placed in a String called "input" Lastly, clear(view) is called
    and user input is printed into printView by using result.setText(input).*/
    public void buttonLoad(View view)
    {
        Scanner fileinput = null;
        File inFile = file;
        String input = "";

        try
        {
            fileinput = new Scanner(inFile);
            while(fileinput.hasNext())
            {
                input += fileinput.nextLine() + "\n";
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            fileinput.close();
        }

        clear(view);
        result.setText(input);
    }

    /*When user hits the "ADD TO END" button, the buttonAddToEnd method is called at which time the whole user
    * input in editText(ed) is appended to the end of File file. This is possible by  FileWriter writer = new FileWriter(file, true);
    * where putting boolean true in the second parameter makes it possible to .write by appending to file. In this method, line is
    * skipped before anything is appended. Lastly, clear(view) and textView(result) is called, where result is cleared of any
    * text viewable to user.*/
    public void buttonAddToEnd (View view) throws IOException
    {
        FileWriter writer = new FileWriter(file, true);
        writer.write("\n" + ed.getText().toString());
        writer.flush();
        writer.close();
        clear(view);
        result.setText("");
    }

    /* When user hits the "ANAGRAM" button, the buttonAnagram method is called.
     * Purpose of anagram is to take words from an input file(in this case, File file a global variable), and compares
     * it to each other to see if they are anagrams of each other. And anagrams are
     * permutations of the letters in that word. Also, anagram disregards any
     * punctuation and/or upper cases in the words; any punctuations in this case,
     * will be eliminated when stringSort is cast, which eliminates all the
     * punctuations and converts all the words into Strings containing of
     * alphabetical letters. When the program is comparing words, it will use
     * testAnagram algorithm to see if the words are anagrams of each other
     * or not. In the method, any output will be written temporarily in a temporary file File temp(global variable),
     * where the original words, with their punctuations, will be printed on the
     * same lines;however, all the lines will be in alphabetical order determined
     * by the first letter of the first word in each line.
     *
     * Furthermore, buttonAnagram will implement 2 try-catch statements to find errors
     * where the input file is empty or does not exist. Also, it can also find
     * the errors where the temp file cannot be written. Under all these errors,the
     * program will call printStackTrace(). If an word is 12 chars or more, then the program will
     * ignore it. The first try-catch runs through the input file to find if the words exceed 12 chars or more, and if the
     * input file exists; if any of these is true, the first try-catch will call printStackTrace(),
     * and at the end of the first try-catch fileInput will be reseted for the
     * next try-catch. The 2nd try-catch is for output, where it will print anagrams on the
     * same line, or words by themselves. Temp and input is there to compare the words in the
     * loop to see if they are anagrams. The catch phrases at the end will attempt to find
     * where output.txt cannot be created, and again where input file does not exist.
     *
     * In the 2nd try-catch statement, the method uses a nested for loop where the first
     * for loop runs from index 0 and 2nd for loop runs from the first loops index + 1.
     * And testAnagram is called in the 2nd loop to find anagrams for the word in the first
     * index. Upon finding an anagram, method will .write anagrams on the same line
     * with a space between every word.
     *
     * Lastly, clear(view) is called and File file is replaced with the contents in
     * temp.*/
    public void buttonAnagram(View view) throws IOException
    {
        String input;
        Scanner fileInput = null;
        File inFile = file;
        ArrayList<String> list = new ArrayList<>();
        result.setText("");
        FileWriter writer = new FileWriter(temp);

        try
        {
            fileInput = new Scanner(inFile);

            while (fileInput.hasNext())
            {
                input = fileInput.next();

                if (input.length() > 12)
                    continue;

                list.add(input);
            }

            alphabeticalOrder(list);

            try
            {
                for (int i = 0; i < list.size(); i++)
                {
                    String left = removeChar(list.get(i));

                    if (left == "")
                        continue;

                    writer.write(left + " ");

                    for (int j = i + 1; j < list.size(); j++)
                    {
                        String right = removeChar(list.get(j));

                        if (right == "")
                            continue;

                        if (testAnagram(left, right))
                        {
                            writer.write(right + " ");
                            list.set(j, "");
                        }
                    }
                    writer.write("\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                writer.flush();
                writer.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } finally
        {
            fileInput.close();
        }

        clear(view);
        file = temp;
    }

    /*testAnagram method takes two inputs as Strings x and y. First, it sets
     * x and y to lower case and sorts it according to the stringSort method
     * created. Second, the method will check if the lengths of the 2 Strings
     * are not equivalent; if they are, then the method returns false. If the
     * lengths are equivalent, then the method tests if x=y, if they are equal,
     * then the method returns false. If other conditions appear, this method
     * will return false. Since this method is boolean, it will always have one
     * output that is either true or false.*/
    public static boolean testAnagram (String x, String y)
    {
        x = x.toLowerCase();
        x = stringSort(x);
        y = y.toLowerCase();
        y = stringSort(y);

        if (x.length() != y.length())
            return false;
        else if (x.equals(y))
            return true;
        else
            return false;
    }

    /* StringSort method takes an input of String z to lower the cases of all the
     * chars in String, and uses trim() to eliminate spaces in z. Next,
     * each of the chars in z is put in the char array characters and the array
     * is sorted using Arrays.sort into alphabetical order. The for loop then
     * checks for any individual char that doesn't equal lower case alphabets
     * from a-z and replaces them with a empty space. Lastly, characters is converted
     * back into a String which is returned(output).*/
    public static String stringSort (String z)
    {
        z = z.toLowerCase();
        z = z.trim();

        char[] characters = z.toCharArray();

        Arrays.sort(characters);
        for ( int i = 0; i < characters.length; i++)
        {
            if (characters[i] <= 96 || characters[i] >= 123)
                characters[i] = ' ';
        }

        return String.valueOf(characters).trim();
    }

    /*removeChar method takes one parameter of type String and
    converts it to lower case and removes non letter characters in the string.
    The method then returns another String after trim() and replaceAll
    is called from the java String library. replaceAll(" ", "") replaces any space
    characters found with "".*/
    public static String removeChar (String s)
    {
        s = s.toLowerCase();
        char[] characters = s.toCharArray();

        for ( int i = 0; i < characters.length; i++)
        {
            if (characters[i] <= 96 || characters[i] >= 123)
                characters[i] = ' ';
        }

        return String.valueOf(characters).trim().replaceAll(" ", "");
    }

    /*alphabeticalOrder method takes a parameter of ArrayList<String> and returns nothing.
    Then the method sorts the ArrayList<String> in alphabetical order regardless of
    capitalization and characters that aren't letters. Method removes non letter characters
    by calling the removeChar(String) method.*/
    public static void alphabeticalOrder (ArrayList<String> list)
    {
        int minIndex;
        String x, y;
        for (int i = 0; i < list.size(); i++)
        {
            minIndex = i;
            x = list.get(i);
            y = x;

            for (int j = i + 1; j < list.size(); j++)
            {
                if (removeChar(list.get(minIndex)).compareToIgnoreCase(removeChar(list.get(j))) > 0)
                {
                    x = list.get(j);
                    minIndex = j;
                }
            }
            list.add(i, x);
            list.remove(i + 1);
            list.add(minIndex, y);
            list.remove(minIndex + 1);
        }
        list.trimToSize();
    }

    public boolean fileExists()
    {
        if(file == null || !file.exists())
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}