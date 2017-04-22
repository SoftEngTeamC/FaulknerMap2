package controller;

import javafx.scene.control.TextArea;
import model.Hours;
import pathfinding.Map;
import service.NodeService;

public class LanguageController {

    public static String[] initializeTextPathfinding(int language){
        String[] directions = new String[20];
        switch (language){
            case 1:

                return directions;
            case 2:

                return directions;
            case 3:

                return directions;
            case 4:

                return directions;
            case 5:

                return directions;
            case 6:

                return directions;
            case 7:

                return directions;
            default:
                return initializeTextPathfinding(1); //default English
        }
    }

    /**
     * Function for main screen help button
     */
    static int HandleHelpButton(int language, TextArea StartInfo_TextArea,
                                Hours hours) {
        //TODO: change once we set what te public void HandleHelpButton() {
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                StartInfo_TextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517\n\n"
                        + "Hospital Operating Hour:\n" +
                        "Morning Hours: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Evening Hours: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 2: //spanish
                StartInfo_TextArea.setText("Para contactar a un empleado\n" +
                        "porfavor llame 774-278-8517\n\n"
                        + "Horas de operacíon:\n" +
                        "Mañana : " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Atardecer : " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 3: //chinese
                StartInfo_TextArea.setText("拨打电话 774-278-8517 呼叫医院工作人员\n\n"
                        + "医院营业时间:\n" +
                        "白日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜晚: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 4: //french
                StartInfo_TextArea.setText("Contactez un employé de l'hôpital\n" +
                        "appelez s'il vous plaît 774-278-8517\n\n"
                        + "Heures d'ouverture:\n" +
                        "Matin: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Soir: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 5: //Italian
                StartInfo_TextArea.setText("Per contattare un dipendente dell'ospedale\n" +
                        "chiamare 774-278-8517\n\n"
                        + "Ore di servizio:\n" +
                        "Mattina: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Notte: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 6: //Japanese
                StartInfo_TextArea.setText("病院のスタッフを呼び出し、電話番号：774-278-8617\n\n"
                        + "病院ビジネス時間:\n" +
                        "日: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "夜: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            case 7: //Portuguese
                StartInfo_TextArea.setText("Para entrar em contato com um funcionário do hospital\n" +
                        "ligue para 774-278-8517\n\n"
                        + "horas de operação:\n" +
                        "Manhã: " + hours.hours1 + ":" + hours.minutes1 + " " + hours.ampm1 + "-" +
                        hours.hours2 + ":" + hours.minutes2 + " " + hours.ampm2 + "\n" +
                        "Tarde: " + hours.hours3 + ":" + hours.minutes3 + " " + hours.ampm3 + "-" +
                        hours.hours4 + ":" + hours.minutes4 + " " + hours.ampm4);
                return language;
            default:
                StartInfo_TextArea.setText("To contact a hospital worker\n" +
                        "please call 774-278-8517");
                return 1;
        }
    }

    //function for Panic Button
    static int HandlePanicButton(TextArea StartInfo_TextArea, NodeService nodeService,
                                  int language) {
        StartInfo_TextArea.setText("Don't Panic");
        Map map = new Map(nodeService.getAllNodes());
        // 1: english, 2: spanish, 3: chinese, 4: french
        //TODO: change once we set what text will actually be shown here
        switch (language) {
            case 1: //english
                StartInfo_TextArea.setText("Don't Panic! Call 774-278-8517!");
               return language;
            case 2: //spanish
                StartInfo_TextArea.setText("No se preocupe");
                return language;
            case 3: //chinese
                StartInfo_TextArea.setText("不要惊慌");
                return language;
            case 4: //french
                StartInfo_TextArea.setText("Ne paniquez pas");
                return language;
            case 5: //Italian
                StartInfo_TextArea.setText("Non fatevi prendere dal panico");
                return language;
            case 6: //Japanese
                StartInfo_TextArea.setText("パニックしないでください");
                return language;
            case 7: //Portuguese
                StartInfo_TextArea.setText("Não entre em pânico");
                return language;
            default:
                StartInfo_TextArea.setText("Don't Panic");
                return 1;
        }
//        DisplayMap(PathFinder.shortestPath(map.getNode(nodeService.findNodeByName("hallway19").getId()), map.getNode(nodeService.findNodeByName("Emergency Department").getId())));
        //TODO: We should not be hardcoding the current kiosk.
    }
}
