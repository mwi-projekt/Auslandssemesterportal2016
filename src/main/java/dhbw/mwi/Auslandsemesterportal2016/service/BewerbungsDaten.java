package dhbw.mwi.Auslandsemesterportal2016.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BewerbungsDaten {

    private String name;
    private String geburtsdatum;
    private String email;
    private String studiengang;
    private final String studiengangsleitung = "Prof. Dr. Thomas Freytag";
    private String aktuellesSemester;
    private String uniPrio1;
    private String uniPrio2;
    private String uniPrio3;
    private final boolean einwilligungUnternehmen = true;
    private final boolean einwilligungStudiengangsleiter = true;
    private String benachteiligung;
    private boolean einverstaendnisBericht;
}
