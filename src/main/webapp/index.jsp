<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="partials/header.html" %>
<link rel="stylesheet" href="node_modules/@fortawesome/fontawesome-free/css/fontawesome.min.css">
<link rel="stylesheet" href="node_modules/@fortawesome/fontawesome-free/css/solid.min.css">
<link rel="stylesheet" type="text/css" href="css/main.css"/>
<link rel="stylesheet" type="text/css" href="css/admin/landingpage.css"/>
<link
        rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
        integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
        crossorigin="anonymous"
/>
<script src="js/pages/index.js"></script>

<!-- Mittlerer Teil --------------------------------------------------------------------------->

<div id="adminBereich">
    <%@ include file="HTML/admin/landing-page.html" %>
    <%@ include file="partials/footer.html" %>
</div>

<div class="inhalt" id="normalBereich">
    <!-- Navigation -->
    <div class="pos-f-t">
        <div class="collapse" id="navbarToggleExternalContent">
            <div class="bg-dark p-4">
                <a class="text-white" href="#auslandsAngebote">Auslandsangebote</a>
                <br/>
                <br/>
                <a class="text-white" href="#erfahrungsBerichte">Erfahrungsberichte</a>
                <br/>
                <br/>
                <a class="text-white" href="bewerbungsportal.html">Meine Bewerbungen</a>
            </div>
        </div>
        <nav class="navbar navbar-dark bg-dark">
            <button
                    class="navbar-toggler"
                    type="button"
                    data-toggle="collapse"
                    data-target="#navbarToggleExternalContent"
                    aria-controls="navbarToggleExternalContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
            >
                <span class="navbar-toggler-icon"></span>
            </button>
        </nav>
    </div>

    <!-- Karussell -->
    <div
            id="carouselExampleIndicators"
            class="carousel slide"
            data-ride="carousel"
    >
        <ol class="carousel-indicators">
            <li
                    data-target="#carouselExampleIndicators"
                    data-slide-to="0"
                    class="active"
            ></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img class="d-block w-100" src="images/SanDiego_Hintergrund.png" alt="First slide"/>
            </div>
            <div class="carousel-item">
                <img class="d-block w-100" src="images/Teipei_Hintergrund.png" alt="Second slide"/>
            </div>
            <div class="carousel-item">
                <img class="d-block w-100" src="images/Teiwan_Hintergrund.png" alt="Third slide"/>
            </div>
        </div>
        <a
                class="carousel-control-prev"
                href="#carouselExampleIndicators"
                role="button"
                data-slide="prev"
        >
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a
                class="carousel-control-next"
                href="#carouselExampleIndicators"
                role="button"
                data-slide="next"
        >
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <br/>

    <!-- Auslandsangebote -->
    <div class="auslandsangebote-container" id="auslandsAngebote">
        <div class="grid-container">
            <div>
                <div class="auslandsangebote-heading-container">
                    <h1 class="auslandsangebote-heading">Auslandsangebote</h1>
                </div>
                <a class="auslandsangebote-href" href="details-auslandsangebote.html">
                    <div class="auslandsangebote-image-left-text-right ">
                        <img src="images/Dundee.png"/>
                        <div class="auslandsangebote-text-block">
                            <br>
                            <h4>
                                University of Abertay Dundee - Schottland
                            </h4>
                            <br>
                            <p>
                                Studiere im Land der Highlands, Gletschertäler und Seen und schnuppere Luft an einer britischen Universität.
                            </p>
                            <ul>
                                <li>Moderne Universität mit ca. 5.000 Studierenden</li>
                                <li>Hafen- und Studentenstadt mit großem Kulturangebot</li>
                                <li>Angebotene Studienbereiche: Wirtschaftsinformatik, Betriebswirtschaftslehre</li>
                            </ul>
                        </div>
                    </div>
                </a>
                <a href="details-auslandsangebote.html">
                    <div class="auslandsangebote-text-left-image-right">
                        <div class="auslandsangebote-text-block mobile-order-swap">
                            <br>
                            <h4>
                                American University in Bulgarien
                            </h4>
                            <br>
                            <p>
                                Schnuppere die Luft einer amerikanischen Hochschule innerhalb der Europäischen Union und lerne dabei die reiche Kultur Bulgariens lieben.
                            </p>
                            <ul>
                                <li>Private Hochschule mit nur 1.100 Studierenden</li>
                                <li>Aufgebaut wie eine amerikanische Hochschule</li>
                                <li>Angebotene Studienbereiche: Informatik, Wirtschaftswissenschaften</li>
                            </ul>
                        </div>
                        <img src="images/Bulgaria.png"/>
                    </div>
                </a>
                <a href="details-auslandsangebote.html">
                    <div class="auslandsangebote-image-left-text-right">
                        <img src="images/NDHU.png"/>
                        <div class="auslandsangebote-text-block">
                            <br>
                            <h4>
                                National Dong Hwa University
                            </h4>
                            <br>
                            <p>
                                Entdecke die Moderne und Kultur des asiatischen Inselstaats und genieße Studienqualität auf höchstem Niveau.
                            </p>
                            <ul>
                                <li>Über 10.000 Studierende</li>
                                <li>Campus zwischen Meer und Nationalpark</li>
                                <li>Angebotene Studienbereiche: Wirtschaft, Ingenieurswissenschaften, Informatik</li>
                            </ul>
                        </div>
                    </div>
                </a>
                <a href="details-auslandsangebote.html">
                    <div class="auslandsangebote-text-left-image-right ">
                        <div class="auslandsangebote-text-block mobile-order-swap">
                            <br>
                            <h4>
                                California State University San Marcos
                            </h4>
                            <br>
                            <p>
                                Werde Teil des Lebensstils der amerikanischen Westküste und studiere in den Vereinigten Staaten.
                            </p>
                            <ul>
                                <li>Größtes staatliches Hochschulnetzwerk Kaliforniens</li>
                                <li>Ca. 9.000 Studierende auf dem Campus in San Marcos </li>
                                <li>Angebotene Studienbereiche: Wirtschaftsinformatik</li>
                            </ul>
                        </div>
                        <img src="images/SanMarcos.png"/>
                    </div>
                </a>
            </div>
        </div>
    </div>


    <!--- Erfahrungsberichte ---->
    <div class="erfahrungsBerichte-container" id="erfahrungsBerichte">
        <div class="auslandsangebote-heading-container">
            <h1 class="auslandsangebote-heading">Erfahrungsberichte</h1>
        </div>
        <div class="erfahrungsBerichte-grid-container">
            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

            <div class="erfahrungsBerichte-image-container">
                <img src="images/dundee.jpg" style="width: 100%;">
                <div class="erfahrungsBerichte-text-container"> Sergio Perez</div>
            </div>

        </div>
    </div>
    </br>
    </br>
    <script
            src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"
    ></script>
    <script
            src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"
    ></script>

<%@ include file="partials/footer.html" %>