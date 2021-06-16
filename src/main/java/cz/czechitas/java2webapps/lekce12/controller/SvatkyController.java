package cz.czechitas.java2webapps.lekce12.controller;

import cz.czechitas.java2webapps.lekce12.service.SvatkyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Controller pro zobrazení svátků.
 */
@Controller
public class SvatkyController {
  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d. MMMM yyyy", new Locale("cs", "CZ"));

  private final SvatkyService svatkyService;

  @Autowired
  public SvatkyController(SvatkyService svatkyService) {
    this.svatkyService = svatkyService;
  }

  @GetMapping(path = "/")
  public ModelAndView today() {
    return html(svatkyService.today(), "Dnes");
  }

  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<String> todayJson() {
    return json(svatkyService.today());
  }

  @GetMapping(path = "/tomorrow")
  public ModelAndView tomorrow() {
    return html(svatkyService.tomorrow(), "Zítra");
  }

  @GetMapping(path = "/tomorrow", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<String> tomorrowJson() {
    return json(svatkyService.tomorrow());
  }

  @GetMapping(path = "/{date:[0-9]{4}-[0-9]{2}-[0-9]{2}}")
  public ModelAndView date(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return html(date, date.format(dateFormat));
  }

  @GetMapping(path = "/{date:[0-9]{4}-[0-9]{2}-[0-9]{2}}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<String> dateJson(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return json(date);
  }

  private ModelAndView html(LocalDate date, String textDay) {
    String svatek = svatkyService.spojit(svatkyService.jmeniny(date));
    return new ModelAndView("index")
            .addObject("den", textDay)
            .addObject("svatek", svatek);

  }

  private List<String> json(LocalDate date) {
    return svatkyService.jmeniny(date);
  }
}
