The Reminder Lang
======

This is a language for reminders.

#### Usage

`compile 'me.matteomerola.reminderlang:reminderlang:0.1.0'` with `jcenter()` in the repositories.

```java
List<Lexer.Token> tokens = ;
try {
  String text = "\"Buy milk\" tomorrow at 17";
  Parser parser = new Parser(text, Lexer.lex(text));
  Reminder r = parser.parse();
} catch (ParseException e) {
  // ...
}
```

#### Sample

Run the sample in this way

- `git clone https://github.com/mattmezza/reminder-lang.git .`
- `./gradlew --console plain :sample:run`
- launches the interpreter: write your reminder
- the interpreter will print out the parsed reminder




###### © Matteo Merola