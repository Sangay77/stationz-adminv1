package com.station.admin.setting;

import com.station.admin.FileUploadUtil;
import com.station.common.entity.Currency;
import com.station.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> settings = settingService.listSettings();
        List<Currency> currencyList = currencyRepository.findAllByOrderByNameAsc();
        model.addAttribute("listCurrencies", currencyList);

        for (Setting setting : settings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request, RedirectAttributes ra) throws IOException {

        GeneralSettingBag settingBag = settingService.generalSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValuesFromForm(request,settingBag.list());

        ra.addFlashAttribute("message", "General settings have been saved.");
        return "redirect:/settings";
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> byId = currencyRepository.findById(currencyId);

        byId.ifPresent(currency -> settingBag.updateCurrencySymbol(currency.getSymbol()));
    }

    private static void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);
            String uploadDir = "site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request,List<Setting> listSettings){

        for (Setting setting: listSettings){

            String value=request.getParameter(setting.getKey());
            if (value!=null){
                setting.setValue(value);
            }

        }

        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailServerSettings=settingService.getMailServerSettings();
        updateSettingValuesFromForm(request,mailServerSettings);
        ra.addFlashAttribute("message","Mail Server settings have been saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailTemplateSettings=settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request,mailTemplateSettings);
        ra.addFlashAttribute("message","Mail Templates settings have been saved");
        return "redirect:/settings";
    }


}
