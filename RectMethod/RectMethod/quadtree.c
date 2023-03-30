/*
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_PER_SERVER 100
#define MAX_PLAYERS 10000
#define MAX_LINE_LENGTH 100
#define MAX_SERVERS 1000

// Structures
struct Player {
    int id;
    int x;
    int y;
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
Tree *tree;
Player players[MAX_PLAYERS];
Square *squares[MAX_SERVERS];
Square *servers[MAX_SERVERS];
int player_count, tree_print_count, square_count;
int top_left_x = 0, top_left_y = 0;
int bot_right_x = 800, bot_right_y = 800;
int total = 0, RMax = 100;


// Function Prototypes
void read_info(char *fn, int size);
int *split_info(char *line, int size);
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


// Functions
int main() {
    // Start Timer
    clock_t start_t, end_t;
    double total_t;
    start_t = clock();
    //
    
    // Read Info
    char name[100];
    strcpy(name, "input.txt");
    read_info(name, sizeof(name)/sizeof(char));
    //print_players();
    //printf("Player Count: %d\n", player_count);
    
    // Initialize Tree
    init_tree();
    assign_players();
    //print_tree_start();
    //print_servers();
    //printf("%d\n", total);
    
    end_t = clock();
    total_t = (double)(end_t - start_t) / CLOCKS_PER_SEC;
    
    
    fit();
    //print_servers();
    print_server_id();
    printf("%f\n", total_t);
    return 0;
}

void read_info(char *fn, int size) {
    FILE *file;
    char line[MAX_LINE_LENGTH];
    
    file = fopen(fn, "r");
    if(file == NULL) {
        return;
    }
    int *line_info, i = 0;
    while(fgets(line, MAX_LINE_LENGTH, file)) {
        line_info = split_info(line, sizeof(line)/sizeof(char));
        //printf("%d %d %d\n", line_info[0], line_info[1], line_info[2]);
        players[i].id = line_info[0];
        players[i].x = line_info[1]*10 + RMax;
        players[i].y = line_info[2]*10 + RMax;
        i++;
    }
    player_count = i;
}

// Splits ID, x and y into array elements
int *split_info(char *line, int size) {
    line = line + 1;
    //printf("%s\n", line);
    int arr[3], stat = 0, i = 0;
    arr[0] = atof(line);
    while(stat == 0 && i < size) {
        if(line[i] == ' ') {
            stat = i;
        }
        i++;
    }
    line = line + stat;
    stat = 0;
    arr[1] = atof(line);
    while(stat == 0 && i < size) {
        if(line[i] == ' ') {
            stat = i;
        }
        i++;
    }
    line = line + stat;
    arr[2] = atof(line);
    return arr;
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

int add_players(Square *sq, int x1, int x2, int y1, int y2) {
    int i;
    for(i = 0; i < player_count; i++) {
        if(players[i].x >= sq->x1 && players[i].x < sq->x2 && players[i].y >= sq->y1 && players[i].y < sq->y2) {
            sq->players[sq->player_num] = players[i];
            sq->player_num++;
        }
    }
    return 0;
}

void assign_players() {
    int i;
    Square *rt = tree->root;
    for(i = 0; i < player_count; i++) {
        rt->players[i] = players[i];
    }
    rt->player_num = i;
    if(rt->player_num <= 100) {
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
    if(sq->player_num > 100) {
        split_square(sq);
    } else {
        squares[square_count] = sq;
        square_count++;
    }
    
    if(sq->child1->player_num > 100) {
        split(sq->child1);
    } else {
        squares[square_count] = sq->child1;
        square_count++;
    }
    if(sq->child2->player_num > 100) {
        split(sq->child2);
    } else {
        squares[square_count] = sq->child2;
        square_count++;
    }
    if(sq->child3->player_num > 100) {
        split(sq->child3);
    } else {
        squares[square_count] = sq->child3;
        square_count++;
    }
    if(sq->child4->player_num > 100) {
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
            if(squares[i]->player_num + squares[j]->player_num <= 100){
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
    for(i = temp; i < square_count; i++) {
        free(squares[i]);
    }
    square_count = temp;
}

// PRINTING FUNCTIONS
void print_players() {
    int i;
    for(i = 0; i < player_count; i++) {
        printf("%d %d %d\n", players[i].id, players[i].x, players[i].y);
    }
}

void print_tree_start() {
    Square *rt = tree->root;
    printf("Tree Root: %d %d %d %d %d\n", rt->player_num, rt->x1, rt->x2, rt->y1, rt->y2);
    tree_print_count = 1;
    
    if(rt->child1 != NULL) {
        print_tree(rt->child1);
        print_tree(rt->child2);
        print_tree(rt->child3);
        print_tree(rt->child4);
    }
}

void print_tree(Square *sq) {
    printf("Tree %d: %d %d %d %d %d\n", tree_print_count, sq->player_num, sq->x1, sq->x2, sq->y1, sq->y2);
    tree_print_count++;
    if(sq->child1 != NULL) {
        print_tree(sq->child1);
        print_tree(sq->child2);
        print_tree(sq->child3);
        print_tree(sq->child4);
    } else {
        return;
    }
}

void print_servers() {
    int i;
    for(i = 0; i < square_count; i++) {
        printf("%d: %d\n", i, squares[i]->player_num);
        total = total + squares[i]->player_num;
    }
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

*/
