package me.matteomerola.remindev.ai.model;

import java.util.Date;

public class Reminder {

  private String text;
  private Long when;
  private Repeat repeat;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getWhen() {
    return when;
  }

  public void setWhen(Long when) {
    this.when = when;
  }

  public Repeat getRepeat() {
    return repeat;
  }

  public void setRepeat(Repeat repeat) {
    this.repeat = repeat;
  }

  @Override
  public String toString() {
    return "text: "+this.text+"\nwhen: "+new Date(this.when)+ ((this.repeat != null) ? ("\n"+this.repeat) : "");
  }

}