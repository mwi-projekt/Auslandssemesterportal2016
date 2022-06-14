import {$} from "../js/config";

let faqComponent = {
    template: `
        <div class="inhalt">
            <div class="container">
                <div class="inhaltBox show">
                    <h3>{{question1}}<b class="caret"></b></h3>
                    <p class="weg">{{answer2}}</p>
                </div>
                <div class="inhaltBox show">
                    <h3>{{question2}}<b class="caret"></b></h3>
                    <p class="weg">{{answer2}}</p>
                </div>
                <div class="inhaltBox show">
                    <h3>{{question3}}<b class="caret"></b></h3>
                    <p class="weg">{{answer3}}</p>
                </div>
                <div class="inhaltBox show">
                    <h3>{{question4}}<b class="caret"></b></h3>
                    <p class="weg">
                        {{answer4}}
                        <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a>
                    </p>
                </div>
                <div class="inhaltBox show">
                    <h3>{{question5}}<b class="caret"></b></h3>
                    <p class="weg">
                        {{answer5}}
                        <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a>
                        {{answer5_2}}
                    </p>
                </div>
                <div class="inhaltBox show">
                    <h3>{{question6}}<b class="caret"></b></h3>
                    <p class="weg">
                        {{answer6}} 
                        <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a>
                        {{answer6_2}}
                    </p>
                </div>
            </div>
        </div>

    `,
    data () {
        return {
            question1: "Wer darf sich für ein Auslandsstudium bewerben?",
            question2: "In welchem Studiensemester sollte ich mich für ein Auslandsaufenthalt bewerben?",
            question3: "In welchem Studiensemester sollte das Auslandssemester vollzogen werden?",
            question4: "Wie viel würde ein Auslandssemester kosten?",
            question5: "Besteht die Möglichkeit, finanzielle Hilfe zu beantragen?",
            question6: "Welche Partneruniversitäten stehen zur Verfügung?",
            answer1: "Grundsätzlich ist für alle Studierenden der DHBW Karlsruhe ein Auslandssemester an einer der Partneruniversitäten der DHBW möglich. Notwendig dazu sind die Zustimmung der Ausbildungsfirma und die Zustimmung des Studiengangsleiters.",
            answer2: "Das Studium an der Dualen Hochschule ist kurz. Die Planung eines Auslandsaufenthalts nimmt jedoch\n                        viel Zeit in Anspruch. Es ist also wichtig, den Auslandsaufenthalt möglichst früh zu planen und schon im 1. Semester sollte man wissen, wohin die Reise gehen soll und das Unternehmen sollte um Zustimmung gebeten werden.",
            answer3: "Das Auslandssemester sollte idealerweise im 4. Semester absolviert werden. Der Zeitraum, in dem die Vorlesungen, Seminare und Prüfungen an der ausländischen Hochschule stattfinden, stimmt in der Regel nicht vollständig mit der Theoriephase an der DHBW überein. In der Regel dauert das Theoriesemester an der ausländischen Hochschule, wie auch an den deutschen Universitäten, länger. Das heißt, das Auslandssemester reicht in der Regel in die Praxisphase hinein und\n                        die DHBW-Studierenden müssen sich mit ihren Ausbildungsunternehmen auch in Bezug auf die Semesterzeiten abstimmen.",
            answer4: "Die Kosten für ein Auslandssemester variieren stark vom jeweiligen Standort der Partneruniversität. Genauere Informationen zu den Kosten entnehmen Sie bitte dem Dokument.",
            answer5: "Zur finanziellen Unterstützung des Auslandsaufenthaltes kann die Beantragung eines Sokrates/Erasmus-Stipendiums im Auslandsamt (Höhe ca. 200 Euro monatlich) oder eines Stipendiums der Landesstiftung Baden-W&uuml;rttemberg (Höhe ca. 100-300 Euro monatlich) beantragt werden. Weitere Details zu Finanzierungsmöglichkeiten können im Dokument",
            answer5_2: "eingesehen werden. Des Weiteren bieten viele Partnerunternehmen ihren Studierenden weitere Finanzierungsmöglichkeiten an.",
            answer6: "Wir bieten zahlreiche Universitäten in den unterschiedlichsten Ecken der Welt. Das Angebot erstreckt sich von Australien bis USA. Alle Angebote sind in dem Dokument",
            answer6_2: "aufgelistet."
        }
    },
    methods: {
        expandFAQ: function () {
            $('.show').css('cursor', 'pointer');
            $('.weg').hide();
            $('.show').on('click', function () {
                $('.weg').hide();
                $(this).children('.weg').show();
            });
        },
    },
    created: function () {
        this.expandFAQ();
    },
    mounted: function () {
        this.expandFAQ();
    }
}
export {faqComponent};