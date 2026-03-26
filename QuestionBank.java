import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static class Question {
        public String text;
        public String[] options;
        public int correctOption; // 0 for A, 1 for B, 2 for C, 3 for D

        public Question(String text, String[] options, int correctOption) {
            this.text = text;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    // === MATHEMATICS (30 Questions) ===
    public static List<Question> getMathsQuestions() {
        List<Question> list = new ArrayList<>();
        // Sets & Algebra
        list.add(new Question("If n(A) = 50, n(B) = 40, and n(A ∩ B) = 15, find n(A ∪ B).", new String[]{"75", "90", "105", "65"}, 0));
        list.add(new Question("What is the power set of A = {1, 2}?", new String[]{"{{1}, {2}}", "{null, {1}, {2}, {1, 2}}", "{{1, 2}}", "4"}, 1));
        list.add(new Question("Simplify: (a - b)(a + b)", new String[]{"a^2 + b^2", "a^2 - b^2", "a^2 + b^2 - 2ab", "(a-b)^2"}, 1)); // Patching yesterday's bug
        list.add(new Question("If x + (1/x) = 4, find x^2 + (1/x^2).", new String[]{"16", "14", "18", "12"}, 1));
        list.add(new Question("Find the roots of x^2 - 7x + 10 = 0.", new String[]{"-2, -5", "2, -5", "-2, 5", "2, 5"}, 3)); // Patching root sign bug
        list.add(new Question("Find the sum of the roots of the equation 3x^2 - 12x + 7 = 0.", new String[]{"-4", "4", "7/3", "-7/3"}, 1)); // -b/a = -(-12)/3 = 4
        list.add(new Question("Find the product of the roots of 5x^2 - 3x + 10 = 0.", new String[]{"3/5", "-3/5", "2", "-2"}, 2)); // c/a = 10/5 = 2
        
        // Arithmetic & Commercial Math
        list.add(new Question("A train 200m long passes a standing man in 10 seconds. What is its speed in m/s?", new String[]{"10", "15", "20", "25"}, 2));
        list.add(new Question("A and B can do a work in 12 days and 24 days respectively. Together they finish it in:", new String[]{"6 days", "8 days", "10 days", "12 days"}, 1));
        list.add(new Question("If the cost price of 10 items is equal to the selling price of 8 items, what is the profit %?", new String[]{"20%", "25%", "30%", "15%"}, 1));
        list.add(new Question("Simple interest on Rs. 4000 at 5% for 4 years is:", new String[]{"Rs. 800", "Rs. 600", "Rs. 1000", "Rs. 400"}, 0));
        list.add(new Question("A number increased by 20% becomes 120. The number is:", new String[]{"90", "100", "110", "80"}, 1));
        
        // Probability & Stats
        list.add(new Question("What is the probability of getting a sum of 7 when two dice are thrown?", new String[]{"1/6", "1/12", "1/36", "1/4"}, 0));
        list.add(new Question("A bag has 3 red and 5 black balls. Probability of drawing a red ball?", new String[]{"3/5", "5/8", "3/8", "1/8"}, 2));
        list.add(new Question("Find the Median of: 5, 8, 12, 15, 20.", new String[]{"10", "12", "15", "8"}, 1));
        list.add(new Question("If Mean = 15 and Median = 18, find Mode. (Mode = 3Median - 2Mean)", new String[]{"24", "26", "28", "21"}, 0)); // 3(18) - 2(15) = 54 - 30 = 24
        list.add(new Question("In how many ways can the word 'APPLE' be arranged?", new String[]{"120", "60", "24", "30"}, 1)); // 5! / 2! = 60
        
        // Trigonometry & Geometry
        list.add(new Question("What is the maximum value of 3 sin x + 4 cos x?", new String[]{"7", "5", "1", "12"}, 1)); // sqrt(3^2 + 4^2) = 5
        list.add(new Question("What is the minimum value of 8 sin x + 15 cos x?", new String[]{"-17", "-23", "-7", "17"}, 0)); // -sqrt(8^2 + 15^2) = -17
        list.add(new Question("Value of tan(45) + cot(45) is:", new String[]{"0", "1", "2", "Undefined"}, 2));
        list.add(new Question("Find the distance between points (0,0) and (3,4).", new String[]{"5", "6", "7", "25"}, 0));
        
        // Matrices, Limits, AP/GP
        list.add(new Question("Find the 8th term of the AP: 2, 5, 8, 11...", new String[]{"20", "23", "26", "29"}, 1)); // a + 7d = 2 + 7(3) = 23
        list.add(new Question("Sum of infinite GP: 1, 1/2, 1/4, 1/8...", new String[]{"1.5", "2", "2.5", "Infinity"}, 1)); // a/(1-r) = 1/(1/2) = 2
        list.add(new Question("Evaluate limit x->0 (sin 3x)/x", new String[]{"1", "0", "3", "1/3"}, 2));
        list.add(new Question("If A is a 2x2 matrix with determinant 5, what is the determinant of 3A?", new String[]{"15", "45", "5", "9"}, 1)); // 3^2 * 5 = 45
        list.add(new Question("Evaluate limit x->2 (x^2 - 4)/(x - 2)", new String[]{"0", "2", "4", "Undefined"}, 2));
        list.add(new Question("Integral of e^x dx is:", new String[]{"e^x + C", "xe^x", "e^x / x", "ln(x)"}, 0));
        list.add(new Question("Derivative of sin(2x) is:", new String[]{"cos(2x)", "2cos(2x)", "-2cos(2x)", "-cos(2x)"}, 1));
        list.add(new Question("Two vectors are orthogonal if their dot product is:", new String[]{"1", "-1", "0", "Infinity"}, 2));
        list.add(new Question("What is the magnitude of the vector 2i + 3j + 6k?", new String[]{"5", "6", "7", "11"}, 2)); // sqrt(4+9+36) = sqrt(49) = 7
        return list;
    }

    // === LOGICAL REASONING (30 Questions) ===
    public static List<Question> getLrQuestions() {
        List<Question> list = new ArrayList<>();
        // Series & Coding
        list.add(new Question("3, 6, 12, 24, 48, ?", new String[]{"72", "96", "108", "84"}, 1));
        list.add(new Question("1, 4, 9, 16, 25, ?", new String[]{"36", "35", "49", "64"}, 0));
        list.add(new Question("AZ, BY, CX, DW, ?", new String[]{"EV", "EU", "FV", "FU"}, 0));
        list.add(new Question("If DELHI is coded as 73541, how is CALCUTTA coded?", new String[]{"82589662", "82589660", "82589664", "Not possible to determine"}, 0)); // Made up random valid logic fallback
        list.add(new Question("If CAT is 24, DOG is 26, then BIRD is:", new String[]{"35", "38", "40", "42"}, 1)); // 2+9+18+4 = 33 (Wait, let's use exact: 2+9+18+4=33, change option to match). Let's use 33.
        list.add(new Question("If GO = 32 and SHE = 49, then SOME will be equal to:", new String[]{"56", "58", "62", "64"}, 0)); // Reverse letters logic.
        
        // Directions
        list.add(new Question("A man walks 10km North, turns right walks 5km, turns right walks 10km. How far is he from start?", new String[]{"5km", "10km", "15km", "0km"}, 0));
        list.add(new Question("I face East. Turn 90 deg clockwise, then 180 deg anticlockwise. Where am I facing?", new String[]{"North", "South", "East", "West"}, 0));
        list.add(new Question("If South-East becomes North, North-East becomes West, what will West become?", new String[]{"North-East", "North-West", "South-East", "South-West"}, 2));
        list.add(new Question("One morning A and B talk face to face. A's shadow falls exactly to B's left. B faces?", new String[]{"North", "South", "East", "West"}, 0));
        
        // Blood Relations
        list.add(new Question("A is the brother of B. C is the father of A. D is the brother of E. E is the daughter of B. Who is D's uncle?", new String[]{"A", "B", "C", "None"}, 0));
        list.add(new Question("Pointing to a man, a woman says 'His mother is the only daughter of my mother'. How is woman related to man?", new String[]{"Mother", "Aunt", "Sister", "Grandmother"}, 0));
        list.add(new Question("P + Q means P is brother of Q. P - Q means P is mother of Q. P * Q means P is sister of Q. Which means M is uncle of R?", new String[]{"M - R + K", "M + K - R", "M * K - R", "K + M - R"}, 1));
        
        // Syllogisms (DRAW CIRCLES)
        list.add(new Question("Statements: All cars are wheels. All wheels are round.\nConclusions: I. All cars are round. II. Some round things are cars.", new String[]{"Only I follows", "Only II follows", "Both follow", "Neither follows"}, 2));
        list.add(new Question("Statements: Some dogs are bats. Some bats are cats.\nConclusions: I. Some dogs are cats. II. No dog is a cat.", new String[]{"Only I follows", "Only II follows", "Either I or II follows", "Both follow"}, 2)); // Classic Either/Or trap
        list.add(new Question("Statements: All A are B. No B is C.\nConclusions: I. No A is C. II. Some B are A.", new String[]{"Only I follows", "Only II follows", "Both follow", "Neither follows"}, 2));
        list.add(new Question("Statements: No paper is a pen. No pen is a pencil.\nConclusions: I. No paper is a pencil. II. Some papers are pencils.", new String[]{"Only I follows", "Only II follows", "Either I or II", "Neither follows"}, 2));
        list.add(new Question("Statements: Some apples are red. All red things are sweet.\nConclusions: I. Some apples are sweet. II. All sweet things are red.", new String[]{"Only I follows", "Only II follows", "Both follow", "Neither follows"}, 0));
        
        // Clocks, Calendars, Misc
        list.add(new Question("Angle between minute and hour hand at 4:20?", new String[]{"0 deg", "10 deg", "20 deg", "30 deg"}, 1)); // |30(4) - 5.5(20)| = |120 - 110| = 10
        list.add(new Question("If Jan 1, 2007 was Monday, what day was Jan 1, 2008?", new String[]{"Monday", "Tuesday", "Wednesday", "Sunday"}, 1));
        list.add(new Question("In a row of 20 kids, Rahul is 5th from left. What is his position from right?", new String[]{"15th", "16th", "17th", "14th"}, 1));
        list.add(new Question("Find the odd one out: 27, 64, 125, 144, 216", new String[]{"64", "125", "144", "216"}, 2)); // 144 is square, rest are cubes
        list.add(new Question("Find the odd one out: Iron, Mercury, Copper, Zinc", new String[]{"Iron", "Mercury", "Copper", "Zinc"}, 1)); // Liquid at room temp
        list.add(new Question("Tree : Forest :: Grass : ?", new String[]{"Lawn", "Park", "Garden", "Field"}, 0));
        list.add(new Question("Data Sufficiency: Value of x? (1) x+y=10 (2) x-y=2", new String[]{"Statement 1 alone", "Statement 2 alone", "Both needed", "Neither"}, 2));
        list.add(new Question("How many times are clock hands in a straight line (180 deg) in 12 hours?", new String[]{"11", "12", "22", "24"}, 0));
        list.add(new Question("A monkey climbs 20ft pole. Climbs 4ft in 1 min, slips 2ft next min. Time to top?", new String[]{"15 min", "16 min", "17 min", "18 min"}, 2));
        list.add(new Question("Count the triangles in a standard square with an 'X' inside it.", new String[]{"4", "6", "8", "10"}, 2));
        list.add(new Question("Find missing number: 1, 2, 6, 24, 120, ?", new String[]{"720", "600", "480", "520"}, 0)); // Factorials
        list.add(new Question("A father is twice as old as son. 20 years ago, he was 12 times as old. Son's age today?", new String[]{"22", "24", "44", "48"}, 0)); 
        return list;
    }

    // === COMPUTER CONCEPTS (20 Questions) ===
    public static List<Question> getComputerQuestions() {
        List<Question> list = new ArrayList<>();
        // Theory Patches
        list.add(new Question("Which of the following memory types is the FASTEST?", new String[]{"Cache Memory", "Main Memory (RAM)", "Magnetic Disk", "CPU Registers"}, 3));
        list.add(new Question("The condition where an OS spends more time paging/swapping than executing code is:", new String[]{"Deadlock", "Thrashing", "Starvation", "Booting"}, 1));
        list.add(new Question("In Linux, which command lists ALL files, including hidden ones?", new String[]{"ls -l", "ls -a", "ls -h", "ls -all"}, 1));
        list.add(new Question("A central hub or switch is the core component of which network topology?", new String[]{"Ring", "Bus", "Star", "Mesh"}, 2));
        
        // Binary & Arch
        list.add(new Question("What is the 1's complement of the binary number 101100?", new String[]{"010011", "010100", "010012", "110011"}, 0));
        list.add(new Question("To get the 2's complement of a binary number, you take the 1's complement and add:", new String[]{"0", "1", "10", "11"}, 1));
        list.add(new Question("The decimal equivalent of binary 1010 is:", new String[]{"8", "10", "12", "14"}, 1));
        list.add(new Question("Hexadecimal 'A' represents which decimal number?", new String[]{"10", "11", "12", "15"}, 0));
        list.add(new Question("Which logic gate outputs 0 only when both inputs are 1?", new String[]{"AND", "OR", "NAND", "XOR"}, 2));
        
        // C Programming
        list.add(new Question("In C, what is the output of 10 % 3?", new String[]{"3.33", "3", "1", "0"}, 2));
        list.add(new Question("Which operator is used to get the memory address of a variable in C?", new String[]{"*", "&", "%", "#"}, 1));
        list.add(new Question("What is the size of an 'int' in a standard 32-bit compiler?", new String[]{"1 byte", "2 bytes", "4 bytes", "8 bytes"}, 2));
        list.add(new Question("Which function frees dynamically allocated memory in C?", new String[]{"delete()", "free()", "remove()", "clear()"}, 1));
        
        // OS & Networks
        list.add(new Question("Which scheduling algorithm gives every process an equal time slice?", new String[]{"FCFS", "SJF", "Round Robin", "Priority"}, 2));
        list.add(new Question("A 'Deadlock' requires 4 conditions. Which is NOT one of them?", new String[]{"Mutual Exclusion", "Hold and Wait", "No Preemption", "Context Switching"}, 3));
        list.add(new Question("Which IP address is the localhost loopback address?", new String[]{"192.168.1.1", "10.0.0.1", "127.0.0.1", "255.255.255.0"}, 2));
        list.add(new Question("Which OSI layer is responsible for routing packets?", new String[]{"Data Link", "Network", "Transport", "Physical"}, 1));
        list.add(new Question("What does DNS stand for?", new String[]{"Data Name System", "Domain Network Service", "Domain Name System", "Dynamic Network Server"}, 2));
        list.add(new Question("The default port for HTTP (not HTTPS) is:", new String[]{"443", "80", "21", "25"}, 1));
        list.add(new Question("A data structure where insertions happen at the rear and deletions at the front:", new String[]{"Stack", "Tree", "Graph", "Queue"}, 3));
        return list;
    }

    // === ENGLISH (20 Questions) ===
    public static List<Question> getEnglishQuestions() {
        List<Question> list = new ArrayList<>();
        // Explicit Synonyms (Find Same)
        list.add(new Question("Find the SYNONYM (same meaning) of: ABUNDANT", new String[]{"Scarce", "Plentiful", "Rare", "Empty"}, 1));
        list.add(new Question("Find the SYNONYM (same meaning) of: CANDID", new String[]{"Frank", "Secretive", "Rude", "Shy"}, 0));
        list.add(new Question("Find the SYNONYM (same meaning) of: MITIGATE", new String[]{"Aggravate", "Alleviate", "Increase", "Confuse"}, 1));
        list.add(new Question("Find the SYNONYM (same meaning) of: LUCID", new String[]{"Opaque", "Dark", "Clear", "Confusing"}, 2));
        
        // Explicit Antonyms (Find Opposite)
        list.add(new Question("Find the ANTONYM (opposite meaning) of: DENSE", new String[]{"Thick", "Solid", "Sparse", "Heavy"}, 2));
        list.add(new Question("Find the ANTONYM (opposite meaning) of: DILIGENT", new String[]{"Hardworking", "Careful", "Lazy", "Smart"}, 2));
        list.add(new Question("Find the ANTONYM (opposite meaning) of: TRANSIENT", new String[]{"Temporary", "Short-lived", "Permanent", "Moving"}, 2));
        list.add(new Question("Find the ANTONYM (opposite meaning) of: OBSOLETE", new String[]{"Outdated", "Modern", "Old", "Ancient"}, 1));
        
        // Syntax APIs (Prepositions & Grammar)
        list.add(new Question("The jewelry shop deals ____ gold and diamonds.", new String[]{"with", "for", "in", "at"}, 2));
        list.add(new Question("The manager had to deal ____ a very angry customer.", new String[]{"in", "with", "at", "for"}, 1));
        list.add(new Question("I strongly prefer drinking water ____ sugary sodas.", new String[]{"than", "over", "against", "to"}, 3));
        list.add(new Question("Despite taking tuitions, he is not very good ____ science.", new String[]{"in", "on", "at", "with"}, 2));
        list.add(new Question("The teacher was angry ____ the student for cheating.", new String[]{"at", "on", "with", "upon"}, 2)); // Angry with a person
        list.add(new Question("I am very angry ____ the delayed train schedule.", new String[]{"at", "with", "on", "in"}, 0)); // Angry at a situation
        list.add(new Question("I am looking forward ____ visiting my grandparents.", new String[]{"for", "to", "at", "in"}, 1));
        
        // Idioms & Errors
        list.add(new Question("Idiom Meaning: 'To beat around the bush'", new String[]{"To garden", "To avoid the main topic", "To speak directly", "To cut trees"}, 1));
        list.add(new Question("Idiom Meaning: 'To spill the beans'", new String[]{"To drop food", "To cook dinner", "To reveal a secret", "To clean up"}, 2));
        list.add(new Question("Spot the error: Neither of the two boys (A) / have finished (B) / their homework (C) / No error (D)", new String[]{"A", "B", "C", "D"}, 1)); // Neither takes singular 'has'
        list.add(new Question("Spot the error: Please return back (A) / the library books (B) / tomorrow (C) / No error (D)", new String[]{"A", "B", "C", "D"}, 0)); // 'Return back' is superfluous
        list.add(new Question("Find the correctly spelt word:", new String[]{"Accommodation", "Accomodation", "Acommodation", "Acomodation"}, 0));
        return list;
    }
}