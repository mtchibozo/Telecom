#ifndef MULTIMEDIA_H
#define MULTIMEDIA_H
#include <iostream>
#include <string>

#include <memory>


class Multimedia        
{
public:

    virtual ~Multimedia() {std::cerr << "adieu monde cruel\n" ;}

    Multimedia();
    Multimedia(std::string newName, std::string newPathname);

    virtual void affichage(std::ostream& out) const {
        out << "Hello INF224, It's been a while" << std::endl;
    }//correspond à la méthode play

    virtual void displayAttributes(std::ostream& out) {
        out << "Name : " << std::endl;
        out << this->getName() << std::endl;
        out << "Pathname : " << std::endl;
        out << this->getPathname() << std::endl;
    }

    std::string getName(){return name;}
    std::string getPathname(){return pathname;}
    void setName(std::string newName){name = newName;}
    void setPathName(std::string newPathname){pathname = newPathname;}

private:
    std::string name;
    std::string pathname;
};

using MultimediaPtr = std::shared_ptr<Multimedia>;


#endif // MULTIMEDIA_H
