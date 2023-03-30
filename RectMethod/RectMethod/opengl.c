
/*
 * Random walk
 *
 * 2022.06.07
 *
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <stdlib.h>
#include <math.h>
#include <stdio.h>
#include <time.h>

float Rmax=100.0;
float alpha=0.0, R=1.0;

float x=0.0, y=0.0;
float x_1, y_1, R_1;
const float PI=3.14;
int counter=0, counter_max=600, counter_start;
int id;
FILE *in_file;


typedef struct {
    float x;
    float y;
} Position ;

Position tseries[30][600];


static void resize(int width, int height)
{
    glViewport(0, 0, width, height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-Rmax, Rmax, -Rmax, Rmax, 1.0, -1.0);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity() ;
}

static void display(void)
{
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glPushMatrix();

    for(int i=0; i<30; i++) {

        glBegin(GL_POINTS);

            glColor3f(1.0/i,0.0,0.2);

            x_1=tseries[i][counter].x;
            y_1=tseries[i][counter].y;

            glVertex2f(x_1, y_1);

        glEnd();

    }

    glPopMatrix();

    glutSwapBuffers();
    glFlush();
}


static void key(unsigned char key, int x, int y)
{
    switch (key)
    {
        case 27 :
        case 'q':

            exit(0);

            break;
    }

    glutPostRedisplay();
}

void timer(int user){

    counter++;

    if (counter>counter_max) {
        counter=0;
    }
    glutPostRedisplay();
    glutTimerFunc(250, timer, 1);

}
 
void init() {
    glClearColor(1,1,1,1);
    glPointSize(8.0);
    glLineWidth(1.0);
    srand(time(NULL));

    in_file=fopen("randomwalk.csv", "r");

    if(in_file == NULL) {
        printf("Error: cannot open randomwalk.csv\n");
        exit(8);
    }
    float p_x, p_y;
    int p_id, p_time=0;

    while(feof(in_file) == 0)   {
        fscanf(in_file, "%d, %d, %f, %f\n", &p_id, &p_time, &p_x, &p_y);

        tseries[p_id][p_time].x=p_x;
        tseries[p_id][p_time].y=p_y;

        if (p_time<10)
            printf("\n(%f, %f)", p_x, p_y);
    }
    fclose(in_file);
}


*/
