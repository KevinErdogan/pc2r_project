//============================================================================
// Name        : cpp_client.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include "Socket.h"

using namespace std;

int main() {
	pc2r::Socket sock;
	sock.connect("localhost", 1234);

	if(sock.isOpen()){
		int a;
		cin >> a;
		return 0;
	}

	return 0;
}
