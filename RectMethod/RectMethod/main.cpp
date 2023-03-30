/*
#include <iostream>
#include <cstring>

// Classes
class Player {
private:
    int x;
    int y;
    int cluster;
    std::string id;
    int cell_row;
    int cell_col;
public:
    // Set
    void setXY(int x, int y) {
        this->x = x;
        this->y = y;
    }
    void setID(std::string id) {
        this->id = id;
    }
    void setCell(int i, int j) {
        cell_row = i;
        cell_col = j;
    }
    void setRow(int r) {
        cell_row = r;
    }
    void setCol(int c) {
        cell_col = c;
    }
    // Print
    void printXY() {
        std::cout << "x: " << x << " y: " << y << "\n";
    }
    void printID() {
        std::cout << id << "\n";
    }
    void printCell() {
        std::cout << "Row: " << cell_row << " Col: " << cell_col << "\n";
    }
    // Get
    int getX() {
        return x;
    }
    int getY() {
        return y;
    }
    int getRow() {
        return cell_row;
    }
    int getCol() {
        return cell_col;
    }
};

class Cell {
private:
    int x1;
    int x2;
    int y1;
    int y2;
    int count;
public:
    Cell() {
        count = 0;
    }
    void setXY(int x1, int x2, int y1, int y2) {
        this->x1 = x1;
        this->x2 = x2;
        this->y1 = y1;
        this->y2 = y2;
    }
    void printXY() {
        std::cout << "x1: " << x1 << " x2: " << x2 << " y1: " << y1 << " y2: " << y2 << "\n";
    }
    void printCount() {
        std::cout << "Count: " << count << "\n";
    }
    void addCount() {
        count++;
    }
    int getCount() {
        return count;
    }
};

// Global Variables
int area_x = 300;
int area_y = 300;
int cell_width = 10; // through x
int cell_height = 10; // through y
int hor_cells = area_x / cell_width;
int ver_cells = area_y / cell_height;

int player_count = 1000;

Cell cells[10000][10000];
Player players[1000];


// Function Prototypes
void init_players();
void print_players();
void sort_players();

void init_cells();
void print_cells();
void assign_cells();
void assign_players();

void make_cluster();



// Functions
int main(int argc, const char * argv[]) {
    init_players();
    //print_players();
    
    init_cells();
    //print_cells();
    
    assign_cells();
    //print_players();
    
    assign_players();
    print_cells();
    return 0;
}

void init_players() {
    int i, x, y;
    std::string id;
    for(i = 0; i < player_count; i++) {
        x = rand() % area_x;
        y = rand() % area_y;
        id = "Player " + std::to_string(i);
        players[i].setXY(x, y);
        players[i].setID(id);
    }
}

void print_players() {
    int i;
    for(i = 0; i < player_count; i++) {
        players[i].printID();
        players[i].printXY();
        players[i].printCell();
        std::cout << "\n";
    }
}

void print_cells() {
    int i, j;
    for(i = 0; i < hor_cells; i++) {
        for(j = 0; j < ver_cells; j++) {
            std::cout << i << " " << j << " ";
            cells[i][j].printXY();
            cells[i][j].printCount();
            std::cout << "\n";
        }
    }
}

void init_cells() {
    int x_cursor = 0;
    int y_cursor = 0;
    int i, j;
    for(i = 0; i < hor_cells; i++) {
        for(j = 0; j < hor_cells; j++) {
            cells[i][j].setXY(x_cursor, x_cursor + cell_width, y_cursor, y_cursor + cell_height);
            y_cursor = y_cursor + cell_height;
        }
        y_cursor = 0;
        x_cursor = x_cursor + cell_width;
    }
}

void assign_cells() {
    int i, temp_x, temp_y, row, col, x_cursor, y_cursor;
    for(i = 0; i < player_count; i++) {
        x_cursor = 0;
        y_cursor = 0;
        temp_x = players[i].getX();
        temp_y = players[i].getY();
        for(row = 0; row < hor_cells; row++) {
            if(temp_x >= x_cursor && temp_x < x_cursor + cell_width) {
                players[i].setRow(row);
                break;
            }
            x_cursor = x_cursor + cell_width;
        }
        for(col = 0; col < ver_cells; col++) {
            if(temp_y >= y_cursor && temp_y < y_cursor + cell_width) {
                players[i].setCol(col);
                break;
            }
            y_cursor = y_cursor + cell_height;
        }
    }
}

void assign_players() {
    int i, r, c;
    for(i = 0; i < player_count; i++) {
        r = players[i].getRow();
        c = players[i].getCol();
        cells[r][c].addCount();
    }
}

*/
