//
//  server.cpp
//  TP C++
//  Eric Lecolinet - Telecom ParisTech - 2016.
//

#include <memory>
#include <string>
#include <iostream>
#include <sstream>
#include "tcpserver.h"
#include "photo.h"
#include "table.h"
#include "video.h"
#include <algorithm>

using namespace std;
using namespace cppu;

const int PORT = 3331;

class MyBase {
public:
  /* Cette méthode est appelée chaque fois qu'il y a une requête à traiter.
   * Ca doit etre une methode de la classe qui gere les données, afin qu'elle
   * puisse y accéder.
   *
   * Arguments:
   * - 'request' contient la requête
   * - 'response' sert à indiquer la réponse qui sera renvoyée au client
   * - si la fonction renvoie false la connexion est close.
   *
   * Cette fonction peut etre appelée en parallele par plusieurs threads (il y a
   * un thread par client).
   *
   * Pour eviter les problemes de concurrence on peut créer un verrou en creant
   * une variable Lock **dans la pile** qui doit etre en mode WRITE (2e argument = true)
   * si la fonction modifie les donnees.
   */
  bool processRequest(TCPConnection& cnx, const string& request, string& response)
  {
    cerr << "\nRequest: '" << request << "'" << endl;

    // 1) pour decouper la requête:
    // on peut par exemple utiliser stringstream et getline()
    
    stringstream ss(request);
    stringstream rep;

    // On sépare le requêtes
    string command;
    string type;
    string name;
    string path;

    // Attribut photo
    string longitude;
    string latitude;

    // Attribut video
    string length;

    // Attributs film
    string length;
    int tableau[10];

    // 2) faire le traitement:
    // - si le traitement modifie les donnees inclure: TCPLock lock(cnx, true);
    // - sinon juste: TCPLock lock(cnx);
    ss >> command;

    if (command == "create" || command == "delete"){
        TCPLock lock(cnx, true);
    }
    else TCPLock lock(cnx);

    if (command == "close")
        return false;

    if (command == "create")
        ss >> type >> name >> path;

    if (command == "read")
        ss >> name;

    if (command  == "display")
        displayAll(rep);

    if (command == "delete"){
        ss >> name;
        deleteByName(name,rep);
    }

    if(type == "photo"){
        ss >> longitude >> latitude;
    }

    else if (type == "video"){
        ss >> total_length;
    }

    else if (type == "film"){
        ss >> total_length >> number_chapter;
        int i = 0;
        while (!ss.eof() && i < 20){
            string current;
            ss >> current;
            *(tableau+i) = stoi(current);
            i++;
        }

    }

    // 3) retourner la reponse au client:
    // - pour l'instant ca retourne juste OK suivi de la requête
    // - pour retourner quelque chose de plus utile on peut appeler la methode print()
    //   des objets ou des groupes en lui passant en argument un stringstream
    // - attention, la requête NE DOIT PAS contenir les caractères \n ou \r car
    //   ils servent à délimiter les messages entre le serveur et le client
    
    if(command == "create"){
        if(type == "photo"){
            createPhoto(name,path,stof(longitude),stof(latitude));
            rep << "Photo bien créée ! " << endl;
        }
        else if (type == "video"){
            createVideo(name,path,stoi(total_length));
            rep << "Vidéo bien créée ! " << endl;
        }
        else if (type == "film"){
            createFilm(name,path,stoi(total_length),stoi(number_chapter),tableau);
            rep << "Film bien créé ! " << endl;
        }
        else if (type == "groupe"){
            createGroupe(name);
            rep << "Groupe bien créé ! " << endl;
        }
    }
    else if (command == "read"){
        affichage(name,rep);
    }

    response = rep.str();

    replace(response.begin(),response.end(),'\n',';');
    //response = "OK: " + request;
    cerr << "response: " << response << endl;
    
    // renvoyer false si on veut clore la connexion avec le client
    return true;
  }
};


int main(int argc, char* argv[])
{
  // cree le TCPServer
  shared_ptr<TCPServer> server(new TCPServer());
  
  // cree l'objet qui gère les données
  shared_ptr<MyBase> base(new MyBase());

  // le serveur appelera cette méthode chaque fois qu'il y a une requête
  server->setCallback(*base, &MyBase::processRequest);
  
  // lance la boucle infinie du serveur
  cout << "Starting Server on port " << PORT << endl;
  int status = server->run(PORT);
  
  // en cas d'erreur
  if (status < 0) {
    cerr << "Could not start Server on port " << PORT << endl;
    return 1;
  }
  
  return 0;
}

