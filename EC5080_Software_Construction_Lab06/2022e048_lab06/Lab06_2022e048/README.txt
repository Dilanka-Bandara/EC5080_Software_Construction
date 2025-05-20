README - EC5080 Lab 06 Submission describe the functions that 

BANDARA H.G.T.D.
2022/E/048
EC5080 - Software Construction
Lab 06 - Input/output, error handling, and parsing formats

--------------------------------------------------
Included Files:
--------------------------------------------------

1. Main.java
   - Contains all three tasks in one program:
     • Task 1: HTML blog post extraction using regex
     • Task 2: XML book parsing using DOM
     • Task 3: JSON user filtering using Gson

 gson-2.10.1.jar
    this is need External library used to parse JSON in Task 3

3. sample.html
   - Input file used for Task 1

4. catalog.xml
   - Input file used for Task 2

5. users.json
   - Input file used for Task 3

6. parsed_books.txt
   - Output of successfully parsed books (Task 2)

7. xml_errors.log
   - Log of invalid or malformed book entries (Task 2)

8. json_errors.log
   - Log of invalid email addresses (Task 3)

--------------------------------------------------
How to Compile and Run:
--------------------------------------------------



1. Open terminal in the project folder.

2. Compile the program:

   i run the code using command prompt then this is the code i used to compile it
   
   javac -cp gson-2.10.1.jar Main.java

   
3. Run the program:
  
   java -cp .;gson-2.10.1.jar Main

   

--------------------------------------------------
Outputs:
--------------------------------------------------

- All blog post entries are printed in the terminal (Task 1).
- Valid books go to parsed_books.txt, and errors to xml_errors.log (Task 2).
- Active users with valid emails are printed; bad ones go to json_errors.log (Task 3).

--------------------------------------------------
Notes:
--------------------------------------------------

- No HTML parsers used for Task 1. Regex only.
- Proper error handling and custom exceptions are used as per instructions.

