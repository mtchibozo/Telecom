#ifndef VIDEO_H
#define VIDEO_H
#include "multimedia.h"
class Video : public Multimedia
{
public:
    Video(){}
    Video(int newLength, std::string newName,std::string newPathname):
        Multimedia(newName,newPathname), length(newLength)
    {
    }

        int getLength(){return length;}

        void setLength(int newLength){length = newLength;}

        virtual void affichage(std::ostream& out) const override {
            //system("mpv "+this->length +" &");}

            system("mpv /cal/homes/tchibozo/inf224/test.png &");}

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
};

using VideoPtr = std::shared_ptr<Video>;


#endif // VIDEO_H
