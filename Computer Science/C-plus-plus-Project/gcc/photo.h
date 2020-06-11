#ifndef PHOTO_H
#define PHOTO_H
#include "multimedia.h"


class Photo : public Multimedia
{
public:
    Photo(){}
    Photo(int newLatitude, int newLongitude, std::string newName,std::string newPathname):
         Multimedia(newName,newPathname), latitude(newLatitude), longitude(newLongitude)
    {
    }
        int getLatitude(){return latitude;}
        int getLongitude(){return longitude;}

        void setLatitude(int newLatitude){latitude = newLatitude;}
        void setLongitude(int newLongitude){longitude = newLongitude;}

        virtual void affichage(std::ostream& out) const override {
            system("imagej /cal/homes/tchibozo/inf224/test.png &");}

        virtual void displayAttributes(std::ostream& out) override{
            out << "Name : " << std::endl;
            out << this->getName() << std::endl;
            out << "Pathname : " << std::endl;
            out << this->getPathname() << std::endl;
            out << "Latitude : " << std::endl;
            out << this->getLatitude() << std::endl;
            out << "Longitude : " << std::endl;
            out << this->getLongitude() << std::endl;
        }

private:
        int latitude;
        int longitude;
};

using PhotoPtr = std::shared_ptr<Multimedia>;

#endif // PHOTO_H
