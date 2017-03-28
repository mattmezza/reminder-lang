package me.matteomerola.remindev.ai;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.matteomerola.remindev.ai.Lexer.Token;
import me.matteomerola.remindev.ai.Lexer.TokenType;
import me.matteomerola.remindev.ai.model.Reminder;
import me.matteomerola.remindev.ai.model.Repeat;
import me.matteomerola.remindev.ai.exceptions.*;

public class Parser {

  private List<Token> tokens;
  private String input;

  public Parser(String input, List<Token> tokens) {
    this.tokens = tokens;
    this.input = input;
  }

  public Reminder parse() throws ParseException {
    Reminder reminder = new Reminder();
    try {
      if (!this.nextTokenIs(TokenType.STRING)) {
        throw new ParseException(this.input, this.readNext(), "Expected STRING found " + this.getNextTokenType());
      }
      String message = this.consumeNext().data;
      reminder.setText(message.substring(1, message.length() - 1));
      Date date = null;
      Date now = new Date();
      Calendar cal = Calendar.getInstance();
      if (this.nextTokenIs(TokenType.NEXT)) {
        Token next = this.consumeNext(); // NEXT
        Token t = this.consumeNext();
        cal.setTime(now);
        switch(t.type) {
          case WEEK: cal.add(Calendar.WEEK_OF_YEAR, 1); cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); break;
          case MONTH: cal.add(Calendar.MONTH, 1); cal.set(Calendar.DAY_OF_MONTH, 1); break;
          case YEAR: cal.add(Calendar.YEAR, 1); cal.set(Calendar.DAY_OF_YEAR, 1); break;
          default: throw new ParseException(this.input, next, "in NEXT only WEEK, MONTH or YEAR are accepted.");
        }
        reminder.setWhen(cal.getTime().getTime());
        if (this.hasToken() && nextTokenIs(TokenType.REPEAT)) {
          this.consumeNext();
          reminder.setRepeat(this.parseRepeat());
        }
        return reminder;
      }
      if (this.nextTokenIs(TokenType.IN)) {
        this.consumeNext(); // IN
        long interval = this.parseInterval();
        cal.setTime(now);
        cal.add(Calendar.SECOND, (int) interval);
        reminder.setWhen(cal.getTime().getTime());
        if (this.hasToken() && nextTokenIs(TokenType.REPEAT)) {
          this.consumeNext();
          reminder.setRepeat(this.parseRepeat());
        }
        return reminder;
      }
      if (this.nextTokenIs(TokenType.TODAY)) {
        this.consumeNext();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY,9);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);
        date = cal.getTime();
        if(!this.hasToken()) {
          reminder.setWhen(date.getTime());
          return reminder;
        }
      }
      if (this.nextTokenIs(TokenType.TOMORROW)) {
        this.consumeNext();
        cal.setTime(now);
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY,9);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);
        date = cal.getTime();
        if(!this.hasToken()) {
          reminder.setWhen(date.getTime());
          return reminder;
        }
      }
      if (this.nextTokenIs(TokenType.TONIGHT)) {
        this.consumeNext();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);
        date = cal.getTime();
        if(!this.hasToken()) {
          reminder.setWhen(date.getTime());
          return reminder;
        }
      }
      if (this.nextTokenIs(TokenType.ON)) {
        // parse date
        Token t = this.consumeNext();
        if (date != null) {
          throw new ParseException(this.input, t, "Cannot set day when using TONIGHT,TODAY,TOMORROW");
        }
        date = this.parseDate();
        if (!this.hasToken()) {
          reminder.setWhen(date.getTime());
          return reminder;
        }
      }
      if (this.nextTokenIs(TokenType.AT)) {
        // parse time
        this.consumeNext();
        date = this.parseTime((date == null) ? now : date);
        if (!this.hasToken()) {
          reminder.setWhen(date.getTime());
          return reminder;
        }
      }
      if (date == null) {
        cal.setTime(now);
        cal.add(Calendar.HOUR, 2);
        date = cal.getTime();
      }
      reminder.setWhen(date.getTime());
      if (this.nextTokenIs(TokenType.REPEAT)) {
        this.consumeNext();
        reminder.setRepeat(this.parseRepeat());
      }
      return reminder;
    } catch (EmptyStackException e) {
      throw new ParseException(this.input, null, "Unterminated statement", e);
    }
  }

  private long parseInterval() throws ParseException, EmptyStackException {
    Token quantity = new Token(TokenType.NUMBER, "1", 0);
    if (this.nextTokenIs(TokenType.NUMBER)) {
      quantity = this.consumeNext();
    }
    Token interval = this.consumeNext();
    if (
      interval.type != TokenType.MONTH &&
      interval.type != TokenType.YEAR &&
      interval.type != TokenType.DAY &&
      interval.type != TokenType.HOUR &&
      interval.type != TokenType.WEEK
    ) {
      throw new ParseException(this.input, interval, "unit of time must be NUMBER (HOUR|DAY|WEEK|MONTH|YEAR)");
    }
    int times = Integer.parseInt(quantity.data);
    long intervalLong = 0;
    switch (interval.type) {
      case YEAR: intervalLong = times * 31557600; break;
      case MONTH: intervalLong = times * 2592000; break;
      case WEEK: intervalLong = times * 604800; break;
      case HOUR: intervalLong = times * 3600; break;
      default: intervalLong = times * 86400;
    }
    return intervalLong;
  }

  private Repeat parseRepeat() throws ParseException, EmptyStackException {
    if (!this.nextTokenIs(TokenType.EVERY)) {
      Token t = this.consumeNext();
      throw new ParseException(this.input, t, "Expected EVERY found " + t.type.name());
    }
    this.consumeNext(); // every
    Repeat r = new Repeat();
    r.setEvery(this.parseInterval());
    if (!this.hasToken()) {
      return r;
    }
    Date untilDate = null;
    if (this.nextTokenIs(TokenType.UNTIL)) {
      this.consumeNext();
      untilDate = this.parseDate();
      if (!this.hasToken()) {
        r.setUntil(untilDate.getTime());
        return r;
      }
      if (!this.nextTokenIs(TokenType.AT)) {
        throw new ParseException(this.input, this.readNext(), "Expected AT found " + this.consumeNext().type.name());
      }
      this.consumeNext();
      r.setUntil(this.parseTime(untilDate).getTime());
      return r;
    }
    return r;
  }

  private Date parseTime(Date day) throws ParseException, EmptyStackException {
    Token hour = this.consumeNext();
    Token sep = new Token(TokenType.TIMESEP, ":", 0);
    if (this.hasToken() && nextTokenIs(TokenType.TIMESEP))
      sep = this.consumeNext();
    Token minutes = new Token(TokenType.NUMBER, "00", 0);
    if (this.hasToken() && this.nextTokenIs(TokenType.NUMBER))
      minutes = this.consumeNext();
    String errMsg = "Time must be in this format hh:mm";
    if (
      hour.type != TokenType.NUMBER ||
      minutes.type != TokenType.NUMBER ||
      sep.type != TokenType.TIMESEP
    ) {
      throw new ParseException(this.input, hour, errMsg);
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(day);
    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.data));
    cal.set(Calendar.MINUTE,Integer.parseInt(minutes.data));
    return cal.getTime();
  }

  private Date parseDate() throws ParseException, EmptyStackException {
    Token day = this.consumeNext();
    Token sep1 = this.consumeNext();
    Token month = this.consumeNext();
    Token sep2 = this.consumeNext();
    Token year = this.consumeNext();
    String errMsg = "Date must be in this format dd/MM/yyyy";
    if (
      day.type != TokenType.NUMBER ||
      month.type != TokenType.NUMBER ||
      year.type != TokenType.NUMBER ||
      sep1.type != TokenType.DATESEP ||
      sep2.type != TokenType.DATESEP
      ) {
      throw new ParseException(this.input, day, errMsg);
    }
    String dateStr = day.data+"/"+month.data+"/"+year.data;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
      Date toReturn = sdf.parse(dateStr);
      Calendar cal = Calendar.getInstance();
      cal.setTime(toReturn);
      cal.set(Calendar.HOUR, 9);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      return cal.getTime();
    } catch (Exception e) {
      throw new ParseException(this.input, day, errMsg, e);
    }
  }

  private Token readNext() throws EmptyStackException {
    try {
      return this.tokens.get(0);
    } catch (IndexOutOfBoundsException e) {
      throw new EmptyStackException();
    }
  }

  private Token consumeNext() throws EmptyStackException {
    // System.out.println("current stack, removing " + this.readNext());
    // for(Token t : this.tokens) {
      // System.err.println(t);
    // }
    return this.tokens.remove(0);
  }

  private boolean nextTokenIs(TokenType tokenType) throws EmptyStackException {
    return this.readNext().type == tokenType;
  }

  private TokenType getNextTokenType() throws EmptyStackException {
    return this.readNext().type;
  }

  private boolean hasToken() {
    return this.tokens.size() > 0;
  }

}