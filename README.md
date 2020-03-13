<h2>Schoolwork at Metropolia UAS. Course: Software Production 1</h2>
<h3>Ohjelmistotuotantoprojekti 1 (TX00CF81-3009)</h3>
<p>Group 6: Pietari Järvi, Jetro Saarti and Kim Widberg.

<h4>Sovelluksen idea</h4>
<p>Sovellus on luotu JavaFX käyttäen ja perusideana on käyttäjän Steam-videopelialustan pelien listaus, jossa käyttäjä voi asettaa pelejä läpäistyksi. Tämän lisäksi sovelluksessa voi myös tarkastella pelien statistiikkaa ja vertailla niitä Steam-kavereiden kanssa.</p>

<h4>Koonti ja testaaminen</h4>
<p>Koonnissa käytämme Mavenia. Testaamiseen käytämme JUnit ja TestFX (UI). Näiden lisäksi käytämme Jenkinsiä automaatiotestaukseen. Jenkins tarvitsee myös OpenJFX ja Monocle jotka on asetettu valmiiksi pom tiedostoon. Jotta testaukset toimisivat oikein, on sovelluksen käytettävä vanhempaa jdk versiota. Havaitsimme, että ainakin 8u181 on toimiva versio. 

JDK:n voit ladata täältä: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html

Latauksen jälkeen JDK on asetettava projektiin (esim. Eclipsessä Project -> Properties -> Java Build Path -> Valitse nykyinen JDK ja paina oikealta edit.

Voit myös samalla tarkistaa, että "Source" tabissa on tarvittavat resurssit (main, test, resources).
</p>

<h4>Tietokanta</h4>
<p>Tietokantaan käytämme Educloud etäpalvelinta yhdessä MariaDB kanssa</p>

<h4>Lombokin asennus</h4>
<p>Sovelluksen kehittämiseen tarvitaan Project Lombok kirjastoa vähentämään toistoa koodissa.

Lombok voidaan ladata täältä: https://projectlombok.org/download

Käynnistä ladattu lombok.jar tiedosto ja aseta IDE:si sijainti "specify location" napilla. Käynnistä tämän jälkeen IDE uudelleen, jos se on päällä.
</p>

<h4>Projektinhallinta</h4>
<p>Projektinhallinta-alustana toimii Nektion
https://www.nektion.com/
</p>
