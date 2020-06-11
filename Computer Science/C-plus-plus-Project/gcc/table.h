#ifndef TABLE_H
#define TABLE_H
#include "multimedia.h"
#include "groupe.h"
#include "photo.h"
#include "video.h"
#include "film.h"
#include <string>
#include <memory>
#include <map>

using namespace std;

class table
{
public:
    table(){}
    table(string name):
        Name(name){}

    PhotoPtr createPhoto(int newLatitude, int newLongitude, std::string newName,std::string newPathname){
        PhotoPtr p (new Photo(newLatitude,newLongitude,newName,newPathname));
        MultimediaTable.insert(std::pair<string,PhotoPtr>(newName,p));
        return p;
    }

    VideoPtr createVideo(int newLength, std::string newName,std::string newPathname){
        VideoPtr v (new Video(newLength, newName, newPathname));
        MultimediaTable.insert(std::pair<string,VideoPtr>(newName,v));
        return v;
    }

    FilmPtr createFilm(int * t,int newLength,std::string newName,std::string newPathname)
{
        FilmPtr f (new Film(t,newLength,newName,newPathname));
        MultimediaTable.insert(std::pair<string,FilmPtr>(newName,f));
        return f;
    }

    GroupePtr createGroup(std::string groupName){
        GroupePtr g (new groupe (groupName));
        GroupTable.insert(std::pair<string,GroupePtr>(groupName,g));
        return g;
    }

    void searchAndDisplayMultimediaAttributes(std::string multimediaName){
        try {
        MultimediaPtr pointer = this->MultimediaTable[multimediaName];
        pointer->displayAttributes(cout);}
        catch (int e){cout << "No item with this name was found in the Multimedia Table." << std::endl;
}
    }

    void searchAndDisplayGroupAttributes(std::string groupName){
        try{
        GroupePtr pointer = this->GroupTable[groupName];
        pointer->displayAttributes(cout);}
        catch (int e) {cout << "No file with this name was found in the Group Table." << std::endl;
                      }
    }


    void searchAndPlay(std::string multimediaName){
        try{
        MultimediaPtr media = this->MultimediaTable[multimediaName];
        media->affichage(cout);}
        catch (int e) {cout << "No item with this name was found in the Multimedia Table." << std::endl;
                      }
    }


private:
    std::map<string,MultimediaPtr> MultimediaTable;
    map<std::string,GroupePtr> GroupTable;
    string Name;
};

using TablePtr = std::shared_ptr<table>;


#endif // TABLE_H
