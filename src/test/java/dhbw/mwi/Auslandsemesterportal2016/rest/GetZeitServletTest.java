package dhbw.mwi.Auslandsemesterportal2016.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetZeitServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<Calendar> calendarMockedStatic;
    private Calendar calendar;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        calendarMockedStatic = mockStatic(Calendar.class);
        calendar = mock(Calendar.class);
    }

    @AfterEach
    void tearDown() {
        calendarMockedStatic.close();
    }

    @Test
    void doGetCorrectTimeSlots() throws IOException {
        int year = 2022;
        calendarMockedStatic.when(Calendar::getInstance).thenReturn(calendar);
        when(calendar.get(Calendar.YEAR)).thenReturn(year);

        new GetZeitServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "[{\"zeit\":\"Sommersemester 2022\"},{\"zeit\":\"Wintersemester 2022\"},{\"zeit\":\"Sommersemester 2023\"},{\"zeit\":\"Wintersemester 2023\"},{\"zeit\":\"Sommersemester 2024\"},{\"zeit\":\"Wintersemester 2024\"}]";
        assertEquals(expected, writer.toString().trim());
    }
}