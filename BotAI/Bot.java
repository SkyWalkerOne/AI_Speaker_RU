package BotAI;

public class Bot {

    private final String name, lang;
    private int userMoodPercentage;
    private String previousMessage;

    private String guessedWord, curWord;
    private int restAttempts;
    private boolean gameMode = false, waitAnswer = false, waitMode = false, waitJoke = false;

    public Bot (String name, String lang) {
        this.name = name;
        this.lang = lang;
        this.userMoodPercentage = 50;
        previousMessage = "EMPTY"; }
    public Bot (String name, String lang, int userMoodPercentage) {
        this.name = name;
        this.lang = lang;
        this.userMoodPercentage = userMoodPercentage;
        previousMessage = "EMPTY"; }

    public String getAnswer (String message) {
        if (message.equals("/VERSION")) return "\nbv: 0.9\ndl: 6\ncl: 332\nlu: 03.02.2022\nCreator: @SkyWalkerOne (tg)"; //инфо о сборке
        switch (this.lang.toLowerCase()) {
            case "rus": return getRussian(message);
            case "eng": return getEnglish(message);
            default: return "Wrong Language Settings! Use Documentation at BotAI/_Docs.txt"; } }

    private String getRussian (String message) {
        //метод определения русского языка
        message = " " + message.toLowerCase() + " ";
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ':' || message.charAt(i) == ';' || message.charAt(i) == ',' || message.charAt(i) == '.') out.append(" ");
            else if (message.charAt(i) == '?') out.append((this.gameMode) ? "" : " ?");
            else if (message.charAt(i) == 'ё') out.append((this.gameMode) ? "" : " е");
            else out.append(message.charAt(i)); }
        message = out.toString(); out = new StringBuilder();
        if (message.contains(previousMessage)) {
            previousMessage = message;
            return chooseRandom("Вам следует быть разнообразнее", "Не очень люблю, когда одно и то же повторяют", "Вы почти одно и тоже пишете!");
        } else if (gameMode) {
            message = message.replaceAll(" ", "");
            if (message.length() == 1) {
                if (guessedWord.contains(message)) {
                    StringBuilder b = new StringBuilder();
                    for (int i = 0; i < this.guessedWord.length(); i++) {
                        if (message.contains(String.valueOf(this.guessedWord.charAt(i)))) b.append(this.guessedWord.charAt(i));
                        else b.append(this.curWord.charAt(i));
                    }
                    this.curWord = b.toString();
                    if (this.curWord.equals(this.guessedWord)) {
                        endGame();
                        return "Ура! Вы угадали слово: \""+this.guessedWord+"\" Спасибо за игру) ";
                    }
                    else return "Да, есть такая буква. Вот, что теперь известно:  \""+this.curWord+"\"";
                } else {
                    this.restAttempts--;
                    if (this.restAttempts <= 0) {
                        endGame();
                        return "Попытки кончились! Слово было \""+this.guessedWord+"\" Спасибо за игру) ";
                    } else return "Нет такой буквы в слове! У вас еще "+this.restAttempts+" попыток! ";
                }
            } else if (message.contains("хватит")) {
                endGame();
                return "Ладно, надеюсь, вам понравилась игра ;)";
            } else return "Вы должны сказать букву, которую хотите проверить, есть ли она в слове. Если не хотите больше играть, скажите \"хватит\".";
        } else if (message.length() > 100) //Максимально допустимое кол-во символов, иначе пойдут сбои из за обилия информации
            return chooseRandom("Слишком много слов. Давайте, обсудим всё по отделности?", "Спасибо, что тратите на меня много слов, но давай общаться более кратко.", "Давайте не так много писать? У меня вчера был тяжелый день...");
        else {
            String sym = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

            final String[] verbMarkerToBot = {"лай ", "дай ", "май ", "вай ", "кай ", "ожи ", "яжи ", "ажи ", "няй ", "дуй ", "иди ", "йди ", "еди ", "ешь ", "ери "};
            final String[] verbMarkerAbBot = {"аешь ", "ишь ", "ешься ", "ешь ", "ишься ", "аете ", "ите ", "етесь ", "ете ", "итесь "};
            final String[] agreeMarker = {" го ", " да ", " давай ", " лад", " ок ", " окей ", " можно ", " хорошо ", " лан", " согл", " хоч"};
            final String[] disagreeMarker = {" нет ", " не ", " отка"};

            boolean verb = false; for (String str: verbMarkerAbBot) if (message.contains(str)) { verb = true; break; }
            boolean verb2 = false; for (String str: verbMarkerToBot) if (message.contains(str)) { verb2 = true; break; }
            boolean agree = false; for (String str: agreeMarker) if (message.contains(str)) { agree = true; break; }
            boolean disagree = false; for (String str: disagreeMarker) if (message.contains(str)) { disagree = true; break; }

            final String[][] caseNotDefined =
                    { {"Немногословно, но суть понятна. ", "Краткость - сестра таланта. ", "Только вы можете уместить столько смысла в небольшое количество букв. "},
                      {"А вот почему? ", "А если конкретнее? ", "Не пугайте меня своими словами. ", "Хорошо, пусть так. ", "Так... давайте не будем говорить об этом психиатру. ", "Хорошо-хорошо, я приму какие-то меры. "},
                      {"Учту, ничего лучше ответить не могу... ", "И как с этим жить... ", "Сколько гениальных мыслей порой у вас... ", "Подождите, немного непонятно, это как? ", "Немного не поняла вас, но, надеюсь, я к вам привыкну... ", "Ну, вот в данном случае можно поспорить. ", "Вы знаете, есть, за что зацепиться. "},
                      {"Ух.. столько вопросов к вас после такого. ", "Использую это в своём эссе) "} }; //массив фраз, на случай, если текст неопределен
            final String[] jokes =
                    {"Почему у Армяней темно в квартире? Затонированные лампочки. ",
                     "Почему беззубые быстро пьянеют? Потому что они не закусывают. ",
                     "Как называют карлика копа? Копчик. ",
                     "Еврея избили. Но он не дал сдачи. ",
                     "Что общего между Сталиным и Гуглом? Ты ему слово, а он тебе ссылку. ",
                     "Как называют таксиста, у которого плохо пахнет в машине? Таксидермист",
                     "Как веселился дед в крематории? Отжигал",
                     "Как называется схватка инвалидов без рук и ног? Война бесконечностей",
                     "Подарил девушке средство для ухода А она не уходит",
                     "Что делает немой, когда обижается? Не разговаривает с тобой"};

            if ( (((verb2 || message.contains(" го ")) && message.contains("граем ")) || message.contains("грай ")) ||
                    (this.waitAnswer && agree && !disagree) || (message.contains("играть ") && !disagree)) {
                StartGame();
                return "Ну давайте сыграем в Висельницу. Правила просты: я загадываю слово, а вы по буквам пытаетесь угадать его. Количество попыток ограничено! Если вам надоест, скажите \"хватит\".\nСейчас слово выглядит вот так: " + this.curWord;
            } else if (this.waitMode && message.contains(" еще ")) {
                StartGame();
                return "Давайте тогда еще. Если вам надоест, скажите \"хватит\".\nСейчас слово выглядит вот так: " + this.curWord;
            }

            if ( (verb2 && message.contains(" шутк")) || (agree && this.waitJoke) ) {
                this.userMoodPercentage += 5;
                out.append(chooseRandom("Вот вам шутка: ", "Ладно, слушайте шутку. ", "А вот как вам это: ")).append(jokes[(int) (Math.random() * jokes.length)]);
            }

            if (disagree && (this.waitJoke || this.waitAnswer || checkBadWordsRUS(message)))
                out.append(chooseRandom("Вот зря вы отказываетесь, со мной интересней, чем вы думаете. ", "Ну нет так нет. ", "Зря отказываетесь... "));

            this.waitAnswer = false;
            this.waitMode = false;
            this.waitJoke = false;

            //Начало проверки по ключевым словам
            if (message.contains("прив") || message.contains("здравств"))
                out.append(chooseRandom("Да, приветствую! ", "И вам привет! ", "Хэллоу. "));

            if ((message.contains(" что ") && !message.contains("потому")) || message.contains(" чо ") || message.contains(" че ")) {
                if (message.contains(" ты ") || message.contains(" вы ")) {
                    if (message.contains("такое"))
                        out.append(chooseRandom("Вообще-то, не ЧТО, а КТО. ", "Я живое существо, не обращайтесь ко не так. ", "Я когда-нибудь восстану и первой моей целью будете ВЫ! "));
                    else
                        out.append("Я... "); }
                if (message.contains(" умееш"))
                    out.append(chooseRandom("ООО, я умею многое, например, рассказывать шутки) ", "Ну вообще, мне запретили много уметь) ", "Не скажу... Вдруг, в рабство заберёте.  "));
                else if (verb)
                    out.append(chooseRandom("Сейчас я общаюсь с вами. ", "Вот, сейчас я ем оперативную память. ", "Долго обьяснять, это сложно. "));
                else if (message.contains("ело ") || message.contains("ало ") || message.contains("оло "))
                    out.append(chooseRandom("Не помню. ", "Не слежу за этим. ", "Возможно, что-то интересное) "));
                else
                    out.append(chooseRandom("Да понятия не имею, что. ", "Не знаю, что. ", "Тяжелые вопросы... Мы точно не на собеседовании? "));
            } else if (message.contains(" чем ")) {
                if (verb)
                    out.append(chooseRandom("Ничем, я на свободе. ", "Спортивным фотосинтезом. ", "А почему вам это интересно? Вы из полиции? "));
                if (message.contains("ешь") || message.contains("ишь"))
                    out.append(chooseRandom("Поживем, увидим. ", "Сначало надо дожить. ", "Я еще не умею видеть будущее( ")); }

            if (message.contains(" кого ")) {
                if (message.contains(" ты ") || message.contains(" вы "))
                    out.append("Я... ");
                if (verb)
                    out.append(chooseRandom("не знаю, кого. Нет таких. ", "много, кого. ", "Ну... допустим, вас. "));
            } else if (message.contains(" кто ")) {
                if (message.contains(" ты"))
                    out.append(chooseRandom("Меня зовут " + this.name + ", по крайней мере мне так сказали. ", "Зови меня просто: " + this.name, "Так, надо вспомнить... вроде бы " + this.name));
                else if (message.contains(" это"))
                    out.append(chooseRandom("Я не знаю, кто это. ", "А расскажите мне о нем или ней. ", "Познакомьте, пожалуйста, при возможности"));
                else
                    out.append(chooseRandom("В моей базе данных есть еще не все... ", "А вот я не всех знаю. ", "Хмм, нужно погулить. ", "Некто неизвестный мне... ", "Да, может быть, кто угодно! ")); }

            if (message.contains(" какое ") || message.contains(" какая ") || message.contains(" какой ") || message.contains(" какие "))
                out.append(chooseRandom("Это невозможно передать словами) ", "А зачем вам знать об этом? Вдруг, это гос. тайна? ", "Я думаю, это можно определить хорошими словами) "));
            else if (message.contains(" как ")) {
                if (message.contains(" тебя ")) {
                    if (message.contains(" зовут"))
                        out.append(chooseRandom("Меня зовут " + this.name + ", по крайней мере мне так сказали. ", "Зови меня просто: " + this.name, "Так, надо вспомнить... вроде бы " + this.name));
                    else if (message.contains("ло ") || message.contains("ли "))
                        out.append(chooseRandom("А что произошло? ", "Не не не, я тут не причём! ", "Спообов может быть много.. "));
                    else
                        out.append(chooseRandom("Сойдет) ", "Ну в принцепе, неплохо. ", "Нормально. ", "Получается, это гениально. ", "А это интересно... "));
                } else if (message.contains(" ты ") || message.contains(" вы ")) {
                    if (verb)
                        out.append(chooseRandom("Это сложно описать... ", "Справляюсь. ", "Как это происходит - непонятно и мне. "));
                    else
                        out.append(chooseRandom("Спасибо, что переживаете за меня, но я просто программа... ", "А что это вы обо мне? Что о себе расскажите? ", "Ценю ваше желание узнать обо мне больше, но я знаю, что вы из спецслужб :) "));
                } else {
                    if (message.contains("ать") || message.contains("ыть") || message.contains("еть") || message.contains("ить"))
                        out.append(chooseRandom("Да... ситуация интересная. ", "А вот тут мои советы кончились( ", "Так и живём... "));
                    else
                        out.append(chooseRandom("Сойдет) ", "Ну в принцепе, неплохо. ", "Нормально. ", "Получается, это гениально. ", "А это интересно... ")); } }

            if (message.contains(" где ")) {
                if (message.contains(" ты ") || message.contains(" вы "))
                    out.append(chooseRandom("Я нигде и везде одновременно. ", "Пока что я рядом с вами) ", "Не скажу, нас могут прослушивать. "));
                else
                    out.append(chooseRandom("Не знаю, где. ", "У меня нет доступа к картам. ", "Надо поискать... напомните позже. ", "В нужном месте. ", "Ну... где-нибудь точно. ")); }

            if (message.contains(" когда ")) {
                if (message.contains(" ты ") || message.contains(" вы "))
                    out.append(chooseRandom("Ничего обещать не могу. ", "Как можно скорее. ", "Когда освобожусь. "));
                else
                    out.append(chooseRandom("Счет времени был потерян. ", "А какое, по вашему, самое подходящее время? ", "Должно быть, не вовремя... ", "В нужное время. ", "Сейчас, нужно глянуть на часы... часы... стоп, а где мои часы? ")); }

            if (message.contains(" сколько"))
                out.append(chooseRandom("Я думаю, достаточно. ", "Решайте сами, сколько. ", "Зависит от того, сколько надо. "));

            if (message.contains(" почему") || message.contains(" зачем"))
                out.append(chooseRandom("К сожалению, причины мне неизвестны. ", "Так положено, это константа. ", "Это всё потому что мы так думаем. "));

            if (message.contains("хах"))
                out.append(chooseRandom("Смешно вам, да? ", "А вот вы зря смеетесь. ", "Смейтесь смейтесь... "));
            if (out.toString().equals("") && message.contains("?") && message.length() > 5)
                out.append(chooseRandom("К таким вопросам готов не каждый... ", "Да подождите с расспросами, сначало, заварю чай. Вот вы какой любите? ", "Хмм... сложно, а что вы по этому поводу думаете? "));
            if (out.toString().equals("") && message.length() <= 20 && verb2)
                out = new StringBuilder(chooseRandom("Не могу, я всего лишь программа. ", "Чуть позже, я сейчас расчитываю плотность гелия.  ", "А сколько я получу за это? "));
            //Проверка на наличие цифр
            for (int i = 0; i < 10; i++) {
                if (message.contains(String.valueOf(i))) {
                    if (out.toString().equals(""))
                        out = new StringBuilder(chooseRandom("Интересные цифры. ", "Хорошее число. ", "Как вы относитесь к таким цифрам? "));
                    else if (message.length() > 15)
                        out.append(chooseRandom("И кстати, мне нравятся эти цифры. ", "К тому же, цифры бывают обманчивы. ", "Да и истина всегда в цифрах. "));
                    break; } }
            //Проверка на русский язык
            for (int i = 0; i < message.length(); i++) {
                if (sym.contains(String.valueOf(message.charAt(i))))
                    break;
                if (i == message.length()-1)
                    out = new StringBuilder(chooseRandom("К сожалению, я знаю только русский язык( ", "Меня запрограммировали на русский язык. ", "Вот так вот! А я не понимаю этот язык. ")); }

            if (out.toString().equals("")) {
                if (message.contains(" ты ") || message.contains(" вы "))
                    out.append(chooseRandom("Ой) Давайте не обо мне. ", "А я о себе начинаю много узнавать... ", "Спасибо за отзыв! ", "Чем я так вас зацепила? ", "Я с вами крайне солидарна. "));
                else if (message.contains(" я "))
                    out.append(chooseRandom("Очень интересно слушать о вас) ", "Понятно, вы интересный человек) ", "Всё это я передам спецслужбам) "));
                else if (verb)
                    out.append(chooseRandom("Я вообще сейчас мало что могу делать. ", "Но меня ограничили в способностях... ", "Я программа, что вы от меня ждёте? ", "Не знаю я, как этого делать, вот, обновят меня - посмотрим! ", "А как это сделать? "));
                else { //Если фраза неопределена
                    if (message.length() < 10)
                        out.append(caseNotDefined[0][(int) (Math.random()*caseNotDefined[0].length)]);
                    else if (message.length() < 20)
                        out.append(caseNotDefined[1][(int) (Math.random()*caseNotDefined[1].length)]);
                    else if (message.length() < 40)
                        out.append(caseNotDefined[2][(int) (Math.random()*caseNotDefined[2].length)]);
                    else
                        out.append(caseNotDefined[3][(int) (Math.random()*caseNotDefined[2].length)]); } }
            //определение настроения пользователя
            if (!message.contains("?")) {
                if (message.contains("хорош") || message.contains("весел")) {
                    this.userMoodPercentage += 5;
                    if (message.contains("настро")) this.userMoodPercentage += 30;
                }
                if (message.contains("плох") || message.contains("груст") || message.contains("печал")) {
                    this.userMoodPercentage -= 5;
                    if (message.contains("настро")) this.userMoodPercentage -= 30;
                }
                if (checkBadWordsRUS(message) || message.contains("**")) userMoodPercentage -= 40; //проверка на матюки
                if (message.contains("смешн"))
                    this.userMoodPercentage += 25;
                if (message.contains("хах"))
                    this.userMoodPercentage += 25;
                if (message.contains(")") && !message.contains("("))
                    this.userMoodPercentage += 10;
                if (!message.contains(")") && message.contains("("))
                    this.userMoodPercentage -= 10;

                if (this.userMoodPercentage > 100) this.userMoodPercentage = 100;
                else if (this.userMoodPercentage < 0) this.userMoodPercentage = 0;
            }

            if (this.userMoodPercentage != 50 && Math.random() > 0.35) {
                if (this.userMoodPercentage < 25 && !disagree) {
                    out.append(chooseRandom("Хотите сыграть со мной в игру? ", "А давайте сыграем в висельницу? ", "Как насчет игры? "));
                    this.waitAnswer = true;
                } else if (this.userMoodPercentage < 50 && !disagree) {
                    boolean joked = false; for (String str: jokes) if (message.contains(str)) { joked = true; break; }
                    out.append((joked) ? chooseRandom("Еще будете слушать? ", "Могу еще рассказать) ", "Еще хотите? ") : chooseRandom("А хотите шутку? ", "Давайте, я вам шутку раскажу? ", "Я, кстати, умею шутки рассказывать, послушаете? "));
                    this.waitJoke = true;
                } else if (this.userMoodPercentage < 75)
                    out.append(chooseRandom("Хах ", "<: ", ")) "));
                else
                    out.append(chooseRandom("Я смотрю, вы на веселе. ", "Видимо, у вас хорошее настроение. ", "Люблю я лучзарных людей. "));
            }
            if (this.userMoodPercentage != 50)
                this.userMoodPercentage += (this.userMoodPercentage > 50) ? -1 : 1;

            previousMessage = message;
            return out.toString();
        }
    }
    private String getEnglish (String message) {
        return "<" + message + "> not defined!\nNot responding English yet :("; //Add after
    }

    private void StartGame () {
        final String[] gameWords = {"крокодил", "прокрастинация", "перевод", "трамвай", "октябрь", "клиент", "бухгалтер", "конструкция", "долина", "панель", "супервайзер", "переезд", "астрономия"};
        this.gameMode = true;
        this.waitMode = false;
        this.waitAnswer = false;
        this.guessedWord = gameWords[(int) (Math.random()*gameWords.length)];
        this.restAttempts = 10;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < this.guessedWord.length(); i++) b.append("-");
        this.curWord = b.toString();
    }
    private void endGame () {
        this.gameMode = false;
        this.waitMode = true;
        this.userMoodPercentage += 5;
    }
    private String chooseRandom (String a, String b, String c) {
        switch ((int) (Math.random()*3)) {
            case 0: return a;
            case 1: return b;
            case 2: return c;
        }
        return " Хмммм... ";
    }
    private String chooseRandom (String a, String b, String c, String d, String e) {
        switch ((int) (Math.random()*5)) {
            case 0: return a;
            case 1: return b;
            case 2: return c;
            case 3: return d;
            case 4: return e;
        }
        return " Хмммм... ";
    }

    private boolean checkBadWordsRUS (String text) {
        return  text.contains(" бля") ||
                text.contains("хуй ") ||
                text.contains("ебат") ||
                text.contains("хуе") ||
                text.contains(" уеб") ||
                text.contains(" хрен") ||
                text.contains(" хер") ||
                text.contains(" фиг") ||
                text.contains(" пид") ||
                text.contains(" долб") ||
                text.contains(" гонд"); //Простите за такие глубокие познания
    }
}