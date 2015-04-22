#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <netdb.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>


int main(int argc, char *argv[]) {

    printf("\n\n");

    int sockid;    // Identificador del socket
    int connid;    // Identificador de la conexion
    int pcon = 1;  // Contador del bucle
    int pa = 0;    // Almacena el numero de puertos que tiene abiertos el pc remoto;

    struct sockaddr_in conect; // Estructora para guardar datos de conexion.

    // Datos en la estructura:
    conect.sin_family = AF_INET;
    conect.sin_addr.s_addr = inet_addr("127.0.0.1"); //IP a escanear
    bzero(&(conect.sin_zero), 8);

    for (pcon = 0; pcon != 8000; pcon++) {

        sockid = socket(AF_INET,SOCK_STREAM,0);

        conect.sin_port = htons(pcon);
        connid = connect(sockid, (struct sockaddr *)&conect, sizeof(struct sockaddr));

        if (connid != -1) {

            printf("Puerto %d.................... ABIERTO \n",pcon);
            pa++;

        }
        close(connid);
        close(sockid);
    }

    printf("\n\n");
    printf("Scann terminado... %d puertos abiertos",pa);
    printf("\n\n");

    return 0;
}
