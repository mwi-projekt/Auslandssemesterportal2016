<head>
    <title>DHBW Auslandsinfo</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="main.css" />
    <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
            integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
            crossorigin="anonymous"
    />
</head>



<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="partials/header.html" %>
<link rel="stylesheet" href="node_modules/@fortawesome/fontawesome-free/css/fontawesome.min.css">
<link rel="stylesheet" href="node_modules/@fortawesome/fontawesome-free/css/solid.min.css">
<script src="js/pages/index.js"></script>

<!-- Mittlerer Teil --------------------------------------------------------------------------->
    <div class="container" id="adminBereich">
        <div class="col-md-12">
            <div class="tile" id="verwaltungIndex">
                <img src="images/homescreen.svg" class="tileImg"> <img src="images/Edit.png" class="smallImg">
                <p>Startseite bearbeiten</p>
            </div>
            <div class="tile" id="verwaltungUser">
                <img src="images/user.svg" class="tileImg"> <img src="images/Edit.png" class="smallImg">
                <p>Benutzer verwalten</p>
            </div>
            <div class="tile" id="verwaltungPortal">
                <img src="images/edit-process.svg" class="tileImg"> <img src="images/Edit.png" class="smallImg">
                <p>Bewerbungsprozess bearbeiten</p>
            </div>
        </div>
    </div>
    
    <a href="#">
		<img src="images/chevron.png" id="chevronup" class="chevronup"  style="opacity:0; transition-duration: 1s;position:fixed; z-index: 999; bottom:25px; right:11%;" alt="Pfeil nach oben" />
	</a>
    
    <div class="inhalt" id="normalBereich">
        <!-- Navigation -->
        <div class="pos-f-t">
            <div class="collapse" id="navbarToggleExternalContent">
                <div class="bg-dark p-4">
                    <a class="text-white" href="#">Auslandsangebote</a>
                    <br />
                    <br />
                    <a class="text-white" href="#">Erfahrungsberichte</a>
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
                    <img class="d-block w-100" src="images/Karussell1.jpg" alt="First slide" />
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="images/Karussell2.jpg" alt="Second slide" />
                </div>
                <div class="carousel-item">
                    <img class="d-block w-100" src="images/Karussell3.jpg" alt="Third slide" />
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


        <!-- Auslandsangebote -->
        <div class="auslandsangebote-container">
            <div class="container">
                <div class="row">
                    <div class="auslandsangebote-heading-container">
                        <h1 class="auslandsangebote-heading">Auslandsangebote</h1>
                    </div>
                    <div class="auslandsangebote-image-left-text-right">
                        <img src="dundee.jpg" class="col-md-6" />
                        <div class="auslandsangebote-text-block col-md-5">
                            <p>
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
                                diam nonumy eirmod tempor invidunt ut labore et dolore magna
                                aliquyam erat, sed diam voluptua. At vero eos et accusam et
                                justo duo dolores et ea rebum. Stet clita
                            </p>
                            <ul>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                            </ul>
                        </div>
                    </div>
                    <div class="auslandsangebote-text-left-image-right">
                        <div class="auslandsangebote-text-block col-md-5 ">
                            <p>
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
                                diam nonumy eirmod tempor invidunt ut labore et dolore magna
                                aliquyam erat, sed diam voluptua. At vero eos et accusam et
                                justo duo dolores et ea rebum. Stet clita
                            </p>
                            <ul>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                            </ul>
                        </div>
                        <img src="dundee.jpg" class="col-md-6 " />
                    </div>
                    <div class="auslandsangebote-image-left-text-right">
                        <img src="dundee.jpg" class="col-md-6" />
                        <div class="auslandsangebote-text-block col-md-5">
                            <p>
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
                                diam nonumy eirmod tempor invidunt ut labore et dolore magna
                                aliquyam erat, sed diam voluptua. At vero eos et accusam et
                                justo duo dolores et ea rebum. Stet clita
                            </p>
                            <ul>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                            </ul>
                        </div>
                    </div>
                    <div class="auslandsangebote-text-left-image-right">
                        <div class="auslandsangebote-text-block col-md-5">
                            <p>
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
                                diam nonumy eirmod tempor invidunt ut labore et dolore magna
                                aliquyam erat, sed diam voluptua. At vero eos et accusam et
                                justo duo dolores et ea rebum. Stet clita
                            </p>
                            <ul>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                            </ul>
                        </div>
                        <img src="dundee.jpg" class="col-md-6" />
                    </div>
                    <div class="auslandsangebote-image-left-text-right">
                        <img src="dundee.jpg" class="col-md-6" />
                        <div class="auslandsangebote-text-block col-md-5">
                            <p>
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
                                diam nonumy eirmod tempor invidunt ut labore et dolore magna
                                aliquyam erat, sed diam voluptua. At vero eos et accusam et
                                justo duo dolores et ea rebum. Stet clita
                            </p>
                            <ul>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                                <li>Nur 5000 Studierende</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script
                src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
                integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
                crossorigin="anonymous"
        ></script>
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


    </div>




<%@ include file="partials/footer.html" %>