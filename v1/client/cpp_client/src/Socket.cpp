#include "Socket.h"
#include <iostream>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <cstring>
#include <string>

#define MAX_SERVBUF 1024
#define MAX_HOSTBUF 1024

using namespace std;

namespace pc2r{

void Socket::connect(const std::string & host, int port){
	addrinfo * addr;
	int addrinfo = getaddrinfo(host.c_str(), NULL, NULL, &addr);
	if(addrinfo < 0){
		perror("getaddrinfo");
		exit(1);
	}
	in_addr ip = ((struct sockaddr_in*)addr->ai_addr)->sin_addr;
	freeaddrinfo(addr);
	connect(ip,port);
}


void Socket::connect(in_addr ipv4, int port){
	if((fd = socket(AF_INET, SOCK_STREAM, 0))==-1){
		perror("Error create socket");
		exit(1);
	}
	sockaddr_in sin;
	sin.sin_family = AF_INET;
	sin.sin_port = htons(port);
	sin.sin_addr = ipv4;
	if(::connect(fd, (sockaddr*) &sin, sizeof(sin)) < 0){
		perror("Error connection to socket");
		::close(fd);
	}
}

/********************************/
void Socket::writeString(std::string msg){
	cout << "writing : " << msg << endl;
	if(::write(fd, msg.c_str() + '\n', msg.length())<=0){
		cout << "pas de print" << endl;
	}
}



/*******************************/
void Socket::close(){
	if(fd!=-1){
		shutdown(fd,2);
		::close(fd);
		fd=-1;
	}
}

std::ostream & operator<< (std::ostream & os, struct sockaddr_in * addr){
	char hostBuff[MAX_HOSTBUF], servBuff[MAX_SERVBUF];
	os << inet_ntoa(addr->sin_addr) << " " << ntohl(addr->sin_port) << " " << getnameinfo((sockaddr*)addr, sizeof(addr), hostBuff, MAX_HOSTBUF, servBuff, MAX_SERVBUF, 0);
	return os;
}

}
