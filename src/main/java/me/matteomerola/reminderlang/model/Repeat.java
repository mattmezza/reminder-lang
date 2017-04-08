package me.matteomerola.reminderlang.model;

import java.util.Date;

public class Repeat {

  private Long every;
  private Long until;

  public Long getEvery() {
    return every;
  }

  public void setEvery(Long every) {
    this.every = every;
  }

  public Long getUntil() {
    return until;
  }

  public void setUntil(Long until) {
    this.until = until;
  }

  @Override
  public String toString() {
    long years = this.every/(3600*24*30*12);
    long months = this.every/(3600*24*30);
    long weeks = this.every/(3600*24*7);
    long days = this.every/(3600*24);
    long hours = this.every/(3600);
    String interval = String.valueOf(hours);
    String unit = "hours";
    if (hours>48) {
      interval = String.valueOf(days);
      unit = "days";
      if (days>14) {
        interval = String.valueOf(weeks);
        unit = "weeks";
        if (weeks>6) {
          interval = String.valueOf(months);
          unit = "months";
          if (months>24) {
            interval = String.valueOf(years);
            unit = "years";
          }
        }
      }
    }
    return "repeat:\n  every: "+interval+" "+ unit + ((this.until != null) ? ("\n  until: " + new Date(this.until)).toString() : "");
  }

}