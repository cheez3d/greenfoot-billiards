[greenfoot]: https://www.greenfoot.org/
[billiards]: https://en.wikipedia.org/wiki/Cue_sports
[demo]: doc/demo.gif

[images]: images/

[spherical-coordinates]: https://en.wikipedia.org/wiki/Spherical_coordinate_system
[ball-texture]: doc/ball-texture.png
[ball]: doc/ball.png
[quaternion-java]: Quaternion.java

[jh-labs-java-image-filters]: https://github.com/ajmas/JH-Labs-Java-Image-Filters
[glow-java]: Glow.java
[texball-java]: TexBall.java
[pocket-java]: Pocket.java
[table-java]: Table.java
[ball-java]: Ball.java
[cue-java]: Cue.java
[glow-types]: doc/glows.png

[point-line-closest]: doc/point-line-closest.png

[circle-circle-collision-before]: doc/circle-circle-collision-before.jpg
[circle-circle-collision-after]: doc/circle-circle-collision-after.jpg

# Billiards
Prin intermediul acestui proiect am explorat capabilitățile [Greenfoot][greenfoot] în ceea ce privește implementarea unui joc de [biliard][billiards]. Acesta încorporează o redare realistă a bilelor, în perspectivă 3D, având în vedere limitarea strictă spre elemente 2D a [Greenfoot][greenfoot]. Mai jos se află o mică demonstrație a proiectului (n.b. afișajul de depanare este activat, astfel că se pot vedea toate _collider_-ele atașate obiectelor):

![demo]

## Generarea _asset_-urilor
Toate _asset_-urile jocului sunt create în cod, în directorul [images][images] al proiectului neexistând imagini prefabricate. Astfel, se pot modifica programatic diverse caracteristici ale obiectelor jocului cum ar fi dimensiunile mesei, dimensiunile bilelor, lungimea tacului etc. printr-o simplă apelare a unei funcții corespunzătoare (e.g. `setWidth`, `setHeight`, `setRadius`).

Două elemente mai interesante de subliniat în ceea ce privește _asset_-urile jocului sunt bilele cu texturi ce creează un efect de tridimensionalitate a acestora (vezi clasa [`TexBall`][texball-java]) și umbrele (vezi clasa [`Glow`][glow-java]), aceste două elemente creând un aspect grafic mai plăcut.

## Sferizarea texturilor bilelor
Pentru realizarea efectului de tridimensionalitate a bilelor s-au folosit ecuațiile următoare, pentru convertirea coordonatelor texturilor la [coordonate sferice][spherical-coordinates]:
`x = r * cos(theta) * sin(phi); y = r * sin(theta) * sin(phi); z = r * cos(phi)`. Pixelii cu `z < 0` nu contribuie la imaginea finală, dat fiind faptul că bilele se văd „de sus”, deci le este vizibilă doar calota superioară.

Textura din imaginea de mai jos este traversată, iar pentru fiecare pixel se calculează o coordonată sferică cu ajutorul unghiurilor `theta` și `phi` corespunzătoare pixelului în cauză:

![ball-texture]

Astfel, pe o altă imagine se desenează rezultatul sferizat al imaginii de mai sus, sub acesta aflându-se un disc alb desenat separat:

![ball]

Pentru rotirea texturilor atunci când bilele se află în mișcare s-au folosit [cuaternioni] (vezi clasa [`Quaternion`][quaternion-java]).


## Generarea umbrelor
Generarea umbrelor se face cu ajutorul bibliotecii externe [JH Labs Java Image Filters][jh-labs-java-image-filters], folosindu-se un filtru de tip `BoxBlurFilter`.

Umbrele poti fi de tip `INNER` (vezi [`Pocket`][pocket-java], [`Table`][table-java]) sau `OUTER` (vezi [`Ball`][ball-java], [`Cue`][cue-java]):

![glow-types]

Efectul de umbră este creat prin construirea unui contur pentru forma obiectului, contur peste care se aplică filtrul de _blur_ cu 3 iterații pentru a crea un efect de _auroră_ în jurul sau în interiorul obiectului, în funcție de tipul umbrei. Culoarea umbrei poate fi schimbată (vezi `setColor` în clasa [`Glow`][glow-java]).

## Fizica
Proiectul se folosește de un mic _physics engine_, referit în codul sursă ca `Physics2`, pentru tot ceea ce înseamnă detecarea și rezolvarea coliziunilor dintre obiecte.

### Detectarea coliziunilor cerc-trapez
![point-line-closest]

Coliziunile cerc-trapez sunt detectate în felul următor: știind vectorii `pt_v` și `seg_v` se proiectează `pt_v` pe `seg_v`, obținându-se vectorul `closest`, care are aceeași origine ca `pt_v`. Acum putem calcula `dist_v` și, comparând modului acestui vector cu raza cercului putem determina dacă are loc o coliziune sau nu:
* `abs(dist_v) > r` => nu are loc o coliziune
* `abs(dist_v) = r` => cercul și trapezul doar se ating
* `abs(dist_v) < r` => are loc o coliziune

### Detectarea coliziunilor cerc-cerc
Coliziunile cerc-cerc se detectează mai simplu, fiindcă pur și simplu comparăm distanța dintre cele două cercuri cu suma razelor celor două cercuri. Dacă distanța e mai mică ca suma razelor cercurilor, atunci a avut loc o coliziune.

### Rezolvarea coliziunilor
Coliziunile cerc-trapez se rezolvă prin reflectarea vectorului viteză al bilei față de normala la planul de coliziune.

Coliziunile cerc-cerc se rezolvă prin despărțirea vectorilor viteză ai bilelor relativ la planul de coliziune într-o componentă normală și o componentă tangențială:

![circle-circle-collision-before]

Componentele tangențiale ale vitezelor rămân neschimbate. În schimb, componentele normale, dat fiind faptul că masele bilelor sunt egale, sunt schimbate între ele:

![circle-circle-collision-after]
