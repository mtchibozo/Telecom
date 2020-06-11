//
// main.cpp
// Created on 21/10/2018
//

#include <iostream>
#include "multimedia.h"
#include "photo.h"
#include "video.h"
#include "film.h"
#include "groupe.h"
#include "table.h"
#include "TCPServer.h"

using namespace std;



int main(int argc, const char* argv[])
{



/*
    MultimediaPtr m (new Multimedia());
    //m->affichage(cout);

    PhotoPtr p ( new Photo());
    //p->affichage(cout);

    VideoPtr v ( new Video(12,"maxime","tchibozo.exe"));
    //v->affichage(cout);
*/

/*
    Multimedia ** tab = new Multimedia * [4];
    unsigned int count = 0;
    tab[count++] = new Video(5,"hey","ho");
    tab[count++] = new Photo(2,3,"hi","bye");
    tab[count++] = new Video(5,"hey","ho");
    tab[count++] = new Photo(5,1,"hey","ho");
    //tab[0]->affichage(cout);
    //tab[1]->affichage(cout);
    //tab[2]->affichage(cout);
    //tab[3]->affichage(cout);
    delete(tab[0]);
    delete(tab[1]);
    delete(tab[2]);
    delete(tab[3]);
    delete(tab);

*/

/*
    count = 0;
    FilmPtr f ( new Film());
    int * lengthsTab = new int [5];
    lengthsTab[count++] = 10;
    lengthsTab[count++] = 9;
    lengthsTab[count++] = 8;
    lengthsTab[count++] = 7;
    lengthsTab[count++] = 6;

    f->setTableOfLengths(lengthsTab);
    //f->affichage(cout);
    //std::cout << lengthsTab << std::endl;
*/

/*
    GroupePtr g (new groupe("mongroupe"));
    g->push_back(m);
    g->push_back(p);
    g->push_back(v);
    g->push_back(f);

    //for (std::list<MultimediaPtr>::iterator it = g->begin();it!=g->end();++it)    (*it)->displayAttributes(cout) ;
    //for (std::list<MultimediaPtr>::iterator it = g->begin();it!=g->end();++it)    (*it)->affichage(cout) ;
*/

/*
    TablePtr t (new table());
    t->createPhoto(2,3,"hi","bye");
    t->createPhoto(4,5,"bye","hi");
    t->searchAndDisplayMultimediaAttributes("hi");
    t->searchAndDisplayMultimediaAttributes("bye");
*/
    TablePtr serverTable = new table();
    serverTable->createPhoto();
    serverTable->createVideo();

    TCPServer* s = new TCPServer(serverTable);

    s->run(3331);

    return 0;

}
