#ifndef GROUPE_H
#define GROUPE_H
#include <list>
#include "string.h"
#include "multimedia.h"
#include <memory>
using namespace std;

class groupe : public std::list<MultimediaPtr>
{

public:

    groupe();
    groupe(string justForShow){groupName = justForShow;}

    string getGroupName(){return groupName;}
    void setGroupName(string newName){groupName = newName;}

    virtual void displayAttributes(std::ostream& out) {
        out << "Name : " << std::endl;
        out << this->getGroupName() << std::endl;
    }

private:

    string groupName;
    iterator it;

};

using GroupePtr = std::shared_ptr<groupe>;


#endif // GROUPE_H
