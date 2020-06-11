#ifndef FILM_H
#define FILM_H
#include "video.h"
#include "string.h"
class Film : public Video
{
public:
    Film(){};

    Film(int * t,int newLength,std::string newName,std::string newPathname):
        Video(newLength,newName,newPathname),tab_de_durees(t)
    {}

        int * getTableOfLengths(){return tab_de_durees;}

        void setTableOfLengths(int * t){tab_de_durees  = t;}

        void setLength(int newLength){length = newLength;}

        int getLength(){return length;}

        virtual void affichage(std::ostream& out) const override {
            for(int i=0;i<5;i++){
            out << tab_de_durees[i] << std::endl;}

                }
        virtual void displayAttributes(std::ostream& out) override{
            out << "Name : " << std::endl;
            out << this->getName() << std::endl;
            out << "Pathname : " << std::endl;
            out << this->getPathname() << std::endl;
            out << "Length : " << std::endl;
            out << this->getLength() << std::endl;
        }


private:
        int length;
        int * tab_de_durees = new int [5];
};

using FilmPtr = std::shared_ptr<Film>;



#endif // FILM_H
