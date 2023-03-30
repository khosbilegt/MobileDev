
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <stdlib.h>
#include <math.h>
#include <stdio.h>
#include <time.h>

#define MAX_PER_SERVER 6
#define MAX_PLAYERS 10000
#define MAX_SERVERS 1000

// Structures
struct Col {
    float r;
    float g;
    float b;
};
typedef struct Col Col;

struct Player {
    int id;
    int x;
    int y;
    float r;
    float g;
    float b;
};
typedef struct Player Player;

struct Square {
    int x1;
    int x2;
    int y1;
    int y2;
    int player_num;
    struct Square *child1;
    struct Square *child2;
    struct Square *child3;
    struct Square *child4;
    Player players[MAX_PLAYERS];
};
typedef struct Square Square;

struct Tree {
    Square *root;
};
typedef struct Tree Tree;


// Global Variables
float Rmax=100.0;
float alpha=0.0, R=1.0;

float x=0.0, y=0.0;
float x_1, y_1, R_1;
const float PI=3.14;
int counter=0, counter_max=600, counter_start;
int id;
Player tseries[100][600];

Tree *tree;
Player players[MAX_PLAYERS];
Square *squares[MAX_SERVERS];
Square *servers[MAX_SERVERS];
int player_count, tree_print_count, square_count;
int top_left_x = 0, top_left_y = 0;
int bot_right_x = 800, bot_right_y = 800;
int total = 0, RMax = 100;

Col col[20];

// Function Prototypes
static void resize(int width, int height);
static void display(void);
static void key(unsigned char key, int x, int y);
void timer(int user);
void init(void);
int getRandom(int lower, int upper);
void init_colors(void);

void print_players(void);
void init_tree(void);
void assign_players(void);
int add_players(Square *sq, int x1, int x2, int y1, int y2);
void split_square(Square *sq);
void split(Square *sq);
void print_tree_start(void);
void print_tree(Square *sq);
void print_servers(void);
void fit(void);
void print_server_id(void);
void write_data(void);
void gen_rgb(int time);

int getRandom(int lower, int upper) {
    int num = (rand() % (upper - lower + 1)) + lower;
    return num;
}

void init_colors() {
    col[1].r = 1;
    col[1].g = 0;
    col[1].b = 0;
    
    col[2].r = 0;
    col[2].g = 0;
    col[2].b = 0;
    
    col[3].r = 0;
    col[3].g = 0;
    col[3].b = 1;
    
    col[4].r = 0;
    col[4].g = 1;
    col[4].b = 0;
    
    col[5].r = 1;
    col[5].g = 1;
    col[5].b = 0;
    
    col[6].r = 0;
    col[6].g = 1;
    col[6].b = 1;
    
    col[7].r = 0.5;
    col[7].g = 0.2;
    col[7].b = 0.1;
    
    col[8].r = 0.7;
    col[8].g = 1;
    col[8].b = 3;
    
    col[9].r = 0;
    col[9].g = 0.9;
    col[9].b = 0.3;
    
    col[10].r = 0;
    col[10].g = 0.9;
    col[10].b = 0.3;
    
    
    col[11].r = 0;
    col[11].g = 1;
    col[11].b = 0;
    
    col[12].r = 1;
    col[12].g = 0;
    col[12].b = 0;
    
    col[13].r = 0.7;
    col[13].g = 1;
    col[13].b = 1;
    
    col[14].r = 0.8;
    col[14].g = 0.4;
    col[14].b = 0.8;
    
    col[15].r = 0.8;
    col[15].g = 0.1;
    col[15].b = 0.3;

    col[16].r = 0.9;
    col[16].g = 0.8;
    col[16].b = 0.9;
    
    col[17].r = 1;
    col[17].g = 1;
    col[17].b = 0.1;
    
    col[18].r = 0;
    col[18].g = 0.9;
    col[18].b = 0.3;
    
    col[19].r = 0;
    col[19].g = 0.9;
    col[19].b = 0.3;
}

void init() {
    init_colors();
    int i, j;
    glClearColor(1,1,1,1);
    glPointSize(8.0);
    glLineWidth(1.0);
    srand(time(NULL));
    
    for(i = 0; i < 100; i++) {
        for(j = 0; j < 600; j++) {
            tseries[i][j].id = i;
            tseries[i][j].x = getRandom(0, 800);
            tseries[i][j].y = getRandom(0, 800);
        }
    }
    
    for(i = 0; i < 600; i++) {
        gen_rgb(i);
    }
}

void gen_rgb(int time) {
    player_count = 100;
    int i, j = 0;
    for(i = 0; i < 100; i++) {
        players[i].id = tseries[i][time].id;
        players[i].x = tseries[i][time].x;
        players[i].y = tseries[i][time].y;
    }
    
    init_tree();
    assign_players();
    //print_server_id();
    fit();
    for(i = 0; i < square_count; i++) {
        // Square Count
        while(j < squares[i]->player_num) {
            tseries[squares[i]->players[j].id][time].r = col[i].r;
            tseries[squares[i]->players[j].id][time].g = col[i].g;
            tseries[squares[i]->players[j].id][time].b = col[i].b;
            
            j++;
        }
        j = 0;
    }
    print_server_id();
    square_count = 0;
    printf("\n\n");
}


static void key(unsigned char key, int x, int y) {
    switch (key) {
        case 27 :
        case 'q':
            exit(0);
            break;
    }

    glutPostRedisplay();
}

static void resize(int width, int height) {
    glViewport(0, 0, width, height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-Rmax, Rmax, -Rmax, Rmax, 1.0, -1.0);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity() ;
}

static void display(void) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glPushMatrix();
    int i;
    for(i = 0; i < 100; i++) {
        glBegin(GL_POINTS);
        //glColor3f(1.0/i,0.0,0.2);
        glColor3f(tseries[i][counter].r, tseries[i][counter].g, tseries[i][counter].b);
        x_1 = tseries[i][counter].x/8 - 50;
        y_1 = tseries[i][counter].y/8 - 50;
        glVertex2f(x_1, y_1);
        glEnd();
    }
    glPopMatrix();
    glutSwapBuffers();
    glFlush();
}

void timer(int user) {
    
    counter++;
    if (counter>counter_max) {
        counter=0;
    }
    glutPostRedisplay();
    glutTimerFunc(1000, timer, 1);

}

int main(int argc, char *argv[]) {
    glutInit(&argc, argv);
    glutInitWindowSize(640,480);
    glutInitWindowPosition(10,10);
    glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);

    glutCreateWindow("Random walk");

    
    init();
    glutReshapeFunc(resize);
    glutDisplayFunc(display);
    glutKeyboardFunc(key);
    glutTimerFunc(33, timer, 1);

    glutMainLoop();

    return EXIT_SUCCESS;
}


void init_tree() {
    tree = malloc(sizeof(Tree));
    Square *square = malloc(sizeof(Square));
    square->x1 = top_left_x;
    square->x2 = bot_right_x;
    square->y1 = top_left_y;
    square->y2 = bot_right_y;
    square->child1 = NULL;
    square->child2 = NULL;
    square->child3 = NULL;
    square->child4 = NULL;
    square->player_num = 0;
    tree->root = square;
}

void assign_players() {
    int i;
    Square *rt = tree->root;
    for(i = 0; i < player_count; i++) {
        rt->players[i] = players[i];
    }
    rt->player_num = i;
    if(rt->player_num <= MAX_PER_SERVER) {
        return;
    } else {
        split(rt);
    }
}


void split_square(Square *sq) {
    sq->child1 = malloc(sizeof(Square));
    sq->child2 = malloc(sizeof(Square));
    sq->child3 = malloc(sizeof(Square));
    sq->child4 = malloc(sizeof(Square));
    
    sq->child1->x1 = sq->x1;
    sq->child1->x2 = sq->x1 + (sq->x2 - sq->x1)/2;
    sq->child1->y1 = sq->y1;
    sq->child1->y2 = sq->y1 + (sq->y2 - sq->y1)/2;
    
    sq->child2->x1 = sq->x1 + (sq->x2 - sq->x1)/2;
    sq->child2->x2 = sq->x2;
    sq->child2->y1 = sq->y1;
    sq->child2->y2 = sq->y1 + (sq->y2 - sq->y1)/2;
    
    sq->child3->x1 = sq->x1;
    sq->child3->x2 = sq->x1 + (sq->x2 - sq->x1)/2;
    sq->child3->y2 = sq->y1 + (sq->y2 - sq->y1)/2;
    sq->child3->y2 = sq->y2;
    
    sq->child4->x1 = sq->x1 + (sq->x2 - sq->x1)/2;
    sq->child4->x2 = sq->x2;
    sq->child4->y2 = sq->y1 + (sq->y2 - sq->y1)/2;
    sq->child4->y2 = sq->y2;
    
    int i;
    for(i = 0; i < sq->player_num; i++) {
        if(sq->child1->x1 <= sq->players[i].x && sq->child1->x2 > sq->players[i].x && sq->child1->y1 <= sq->players[i].y && sq->child1->y2 > sq->players[i].y) {
            sq->child1->players[sq->child1->player_num] = sq->players[i];
            sq->child1->player_num++;
            //printf("Child 1: %d\n", sq->child1->player_num);
        }
        else if(sq->child2->x1 <= sq->players[i].x && sq->child2->x2 > sq->players[i].x && sq->child2->y1 <= sq->players[i].y && sq->child2->y2 > sq->players[i].y) {
            sq->child2->players[sq->child2->player_num] = sq->players[i];
            sq->child2->player_num++;
        }
        else if(sq->child3->x1 <= sq->players[i].x && sq->child3->x2 > sq->players[i].x && sq->child3->y1 <= sq->players[i].y && sq->child3->y2 > sq->players[i].y) {
            sq->child3->players[sq->child3->player_num] = sq->players[i];
            sq->child3->player_num++;
        }
        else if(sq->child4->x1 <= sq->players[i].x && sq->child4->x2 > sq->players[i].x && sq->child4->y1 <= sq->players[i].y && sq->child4->y2 > sq->players[i].y) {
            sq->child4->players[sq->child4->player_num] = sq->players[i];
            sq->child4->player_num++;
        }
    }
}

void split(Square *sq) {
    if(sq->player_num > MAX_PER_SERVER) {
        split_square(sq);
    } else {
        squares[square_count] = sq;
        square_count++;
    }
    
    if(sq->child1->player_num > MAX_PER_SERVER) {
        split(sq->child1);
    } else {
        squares[square_count] = sq->child1;
        square_count++;
    }
    if(sq->child2->player_num > MAX_PER_SERVER) {
        split(sq->child2);
    } else {
        squares[square_count] = sq->child2;
        square_count++;
    }
    if(sq->child3->player_num > MAX_PER_SERVER) {
        split(sq->child3);
    } else {
        squares[square_count] = sq->child3;
        square_count++;
    }
    if(sq->child4->player_num > MAX_PER_SERVER) {
        split(sq->child4);
    } else {
        squares[square_count] = sq->child4;
        square_count++;
    }
    
    return;
}


void fit(){
    int i, j, m, temp = 0;
    for(i = 0; i < square_count; i++){
        for(j = i+1; j < square_count; j++){
            if(squares[i]->player_num + squares[j]->player_num <= MAX_PER_SERVER){
                for(m = squares[i]->player_num; m < squares[i]->player_num + squares[j]->player_num ; m++){
                    squares[i]->players[m].id = squares[j]->players[m-squares[i]->player_num].id;
                    squares[i]->players[m].x = squares[j]->players[m-squares[i]->player_num].x;
                    squares[i]->players[m].y = squares[j]->players[m-squares[i]->player_num].y;
                    squares[j]->players[m-squares[i]->player_num].id = 0;
                    squares[j]->players[m-squares[i]->player_num].x = 0;
                    squares[j]->players[m-squares[i]->player_num].y = 0;
                }
                squares[i]->player_num = squares[i]->player_num + squares[j]->player_num;
                squares[j]->player_num = 0;
            }
        }
    }
    for(i = 0; i < square_count; i++) {
        if(squares[i]->player_num != 0) {
            temp++;
        }
    }
    square_count = temp;
}


void print_server_id() {
    int i, j = 0;
    for(i = 0; i < square_count; i++) {
        printf("%d: ", i);
        while(j < squares[i]->player_num) {
            printf("%d ", squares[i]->players[j].id);
            j++;
        }
        j = 0;
        printf("\n");
    }
}


