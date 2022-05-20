// Box-Komponente
app.component('faq-inhaltBox', {
    template:
    /*html*/
        `
        <div>
            <h1>Hallo</h1>
        </div>
        `,
    data () {
        return {
            questions: [{
                question: 'Wer darf sich für ein Auslandsstudium bewerben?',
                question: 'In welchem Studiensemester sollte ich mich für ein Auslandsaufenthalt bewerben?',
                question: 'In welchem Studiensemester sollte das Auslandssemester vollzogen werden?',
                question: 'Wie viel würde ein Auslandssemester kosten?',
                question: 'Besteht die Möglichkeit, finanzielle Hilfe zu beantragen?',
                question: 'Welche Partneruniversitäten stehen zur Verfügung?',
            }],
            answers: [{
                answer: 'Grundsätzlich ist für alle Studierenden der DHBW Karlsruhe ein Auslandssemester an einer der Partneruniversitäten der DHBW möglich. Notwendig dazu sind die Zustimmung der Ausbildungsfirma und die Zustimmung des Studiengangsleiters.',
                answer: 'Das Studium an der Dualen Hochschule ist kurz. Die Planung eines Auslandsaufenthalts nimmt jedoch\n' +
                    '                        viel Zeit in Anspruch. Es ist also wichtig, den Auslandsaufenthalt möglichst früh zu planen und schon im 1. Semester sollte man wissen, wohin die Reise gehen soll und das Unternehmen sollte um Zustimmung gebeten werden.',
                answer: 'Das Auslandssemester sollte idealerweise im 4. Semester absolviert werden. Der Zeitraum, in dem die Vorlesungen, Seminare und Prüfungen an der ausländischen Hochschule stattfinden, stimmt in der Regel nicht vollständig mit der Theoriephase an der DHBW überein. In der Regel dauert das Theoriesemester an der ausländischen Hochschule, wie auch an den deutschen Universitäten, länger. Das heißt, das Auslandssemester reicht in der Regel in die Praxisphase hinein und\n' +
                    '                    die DHBW-Studierenden müssen sich mit ihren Ausbildungsunternehmen auch in Bezug auf die Semesterzeiten abstimmen.',
                answer: 'Die Kosten für ein Auslandssemester variieren stark vom jeweiligen Standort der Partneruniversität. Genauere Informationen zu den Kosten entnehmen Sie bitte dem Dokument <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a>.',
                answer: 'Zur finanziellen Unterstützung des Auslandsaufenthaltes kann die Beantragung eines Sokrates/Erasmus-Stipendiums im Auslandsamt (Höhe ca. 200 Euro monatlich) oder eines Stipendiums der Landesstiftung Baden-W&uuml;rttemberg (Höhe ca. 100-300 Euro monatlich) beantragt werden. Weitere Details zu Finanzierungsmöglichkeiten können im Dokument <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a> eingesehen werden. Des Weiteren bieten viele Partnerunternehmen ihren Studierenden weitere Finanzierungsmöglichkeiten an.',
                answer: 'Wir bieten zahlreiche Universitäten in den unterschiedlichsten Ecken der Welt. Das Angebot erstreckt sich von Australien bis USA. Alle Angebote sind in dem Dokument <a href="https://karlsruhe.dhbw.de/fileadmin/user_upload/documents/content-de/Auslandsamt/Infobroschuere-Ausland.pdf">"Infobroschüre 2020"</a> aufgelistet.'
            }]

        }
    }
})