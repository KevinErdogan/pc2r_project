//============================================================================
// Name        : cpp_client.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include <string>
#include "Socket.h"

using namespace std;

int main() {
	pc2r::Socket sock;
	sock.connect("127.0.0.1", 1234);

	if(sock.isOpen()){
		while(1){
		 string a;
		 cin >> a;
		 sock.writeString(a);
		}
	}

	return 0;
}
