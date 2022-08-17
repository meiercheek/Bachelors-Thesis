const itemData = [
    {
      "name": "Agaricus_arvensis",
      "author": "Von.grzanka",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Pieczarka_polowa_vongrzanka.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/agaricus_arvensis.jpg"
    },
    {
      "name": "Amanita_muscaria",
      "author": "Onderwijsgek at nl.wikipedia",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/nl/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Amanita_muscaria_3_vliegenzwammen_op_rij.jpg"
      ,
      "img" : process.env.PUBLIC_URL + "/shrooms/amanita_muscaria.jpg"
    },
    {
      "name": "Amanita_pantherina",
      "author": "George Chernilevsky",
      "license": "https://creativecommons.org/publicdomain/mark/1.0/",
      "link": "https://en.wikipedia.org/wiki/File:Amanita_pantherina_2013_G1.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/amanita_pantherina.jpg"
    },
    {
      "name": "Amanita_phalloides",
      "author": "Archenzo",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Amanita_phalloides_1.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/amanita_phalloides.jpg"
    },
    {
      "name": "Auricularia_auricula-judae",
      "author": "Josh Milburn",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Auricularia_auricula-judae_64485.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/auricularia_auricula_judae.jpg"
    },
    {
      "name": "Boletus_reticulatus",
      "author": "I, Rude",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Boletus.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/boletus_reticulatus_edulis.jpg"
    },
    {
      "name": "Calocera_viscosa",
      "author": "1benjones",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Cviscosa_wiki.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/calocera_viscosa.jpg"
    },
    {
      "name": "Cantharellus_cibarius",
      "author": "Strobilomyces",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Chanterelle_Cantharellus_cibarius.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/cantharellus_cibarius.jpg"
    },
    {
      "name": "Chlorociboria_aeruginascens",
      "author": "Sava Krstic",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Chlorociboria_aeruginascens-336907.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/chlorociboria_aeruginascens.jpg"
    },
    {
      "name": "Cortinarius_semisanguineus",
      "author": "Frank Gardiner aka Zonda Grattus",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Cort_semi.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/cortinarius_semisanguineus.jpg"
    },
    {
      "name": "Cortinarius_violaceus",
      "author": "Dan Molter (shroomydan)",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:2008-08-22_Cortinarius_violaceus_(L.)_Gray_18241.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/cortinarius_violaceus.jpg"
    },
    {
      "name": "Galerina_marginata",
      "author": "Alan Rockefeller",
      "license": "https://creativecommons.org/licenses/by-sa/4.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Galerina_marginata_Point_Reyes.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/galerina_marginata.jpg"
    },
    {
      "name": "Gyromitra_esculenta",
      "author": "Severine Meißner",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Fr%C3%BChjahrslorchel.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/gyromitra_esculenta.jpg"
    },
    {
      "name": "Hygrocybe_cantharellus",
      "author": "{{{2}}} at Mushroom Observer",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:2011-06-17_Hygrocybe_cantharellus_69428_cropped.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/hygrocybe_cantharellus.jpg"
    },
    {
      "name": "Lactarius_deliciosus",
      "author": "Eric Steinert",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Lactarius_deliciosus.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/lactarius_deliciosus.jpg"
    },
    {
      "name": "Leccinum_scabrum",
      "author": "Olaf1541",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Birkenpilz01.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/leccinum_scabrum.jpg"
    },
    {
      "name": "Lepista_nuda",
      "author": "Jimmie Veitch (jimmiev)",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Clitocybe_nuda_(Fr.)_H.E._Bigelow_%26_A.H._Sm_267650.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/lepista_nuda.jpg"
    },
    {
      "name": "Macrolepiota_procera",
      "author": "BMT at English Wikipedia",
      "license": "https://creativecommons.org/publicdomain/mark/1.0/",
      "link": "https://en.wikipedia.org/wiki/File:ParasolMushroom.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/macrolepiota_procera.jpg"
    },
    {
      "name": "Pleurotus_ostreatus",
      "author": "Jean-Pol GRANDMONT",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Pleurotus_ostreatus_JPG7.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/pleurotus_ostreatus.jpg"
    },
    {
      "name": "Rubroboletus_satanas",
      "author": "Holger Krisp",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Satans-R%C3%B6hrling_Boletus_satanas.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/rubroboletus_satanas.jpg"
    },
    {
      "name": "Russula_cyanoxantha",
      "author": "Panterka",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://en.wikipedia.org/wiki/File:Russula_cyanoxantha.JPG",
      "img" : process.env.PUBLIC_URL + "/shrooms/russula_cyanoxantha.jpg"
    },
    {
      "name": "Russula_lepida",
      "author": "zaca at Mushroom Observer",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://commons.wikimedia.org/wiki/File:2011-05-14_Russula_lepida_146790.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/russula_lepida.jpg"
    },
    {
      "name": "Sarcoscypha_austriaca",
      "author": "Dan Molter (shroomydan)",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://commons.wikimedia.org/wiki/File:Sarcoscypha_austriaca_81949.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/sarcoscypha_austriaca.jpg"
    },
    {
      "name": "Suillus_luteus",
      "author": "walt sturgeon (Mycowalt)",
      "license": "https://creativecommons.org/licenses/by-sa/3.0/deed.en",
      "link": "https://commons.wikimedia.org/wiki/File:Suillus_luteus_475376.jpg",
      "img" : process.env.PUBLIC_URL + "/shrooms/suillus_luteus.jpg"
    }
  ];


export default itemData