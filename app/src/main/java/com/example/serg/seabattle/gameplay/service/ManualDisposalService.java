package com.example.serg.seabattle.gameplay.service;

import com.example.serg.seabattle.common.enums.CellState;
import com.example.serg.seabattle.common.enums.ColorCellType;
import com.example.serg.seabattle.gameplay.entity.Cell;

public class ManualDisposalService {
    private static ManualDisposalService manualDisposalService;

    private CoordinatesConverterService converterService;
    private ShipService shipService;

    private ManualDisposalService() {
        converterService = CoordinatesConverterService.getCoordinatesConverterService();
        shipService = ShipService.getShipService();
    }

    public static ManualDisposalService getManualDisposalService() {
        if (manualDisposalService == null) {
            manualDisposalService = new ManualDisposalService();
        }
        return manualDisposalService;
    }

    public boolean isSetShipAllowed(Cell[] cells, int position, int deckNumber, boolean orientation) {
        if (deckNumber > 0) {
            if (cells[position].getCellState() == CellState.EMPTY) {
                Cell[][] placementCells = converterService.toPlacementCells(cells);
                int x = converterService.setX(position);
                int y = converterService.setY(position);
                return isCellsAvailable(deckNumber, orientation, placementCells, x, y);
            }
        }

        return false;
    }

    public Cell[] setShip(Cell[] cells, int position, int deckNumber, boolean orientation) {

        Cell[][] placementCells = converterService.toPlacementCells(cells);
        int x = converterService.setX(position);
        int y = converterService.setY(position);

        if (orientation) {
            if (y + deckNumber - 1 < 10) {
                setVerticalShipCells(deckNumber, placementCells, x, y);
            } else {
                y = y - deckNumber + 1;
                setVerticalShipCells(deckNumber, placementCells, x, y);
            }
        } else {
            if (x + deckNumber - 1 < 10) {
                setHorizontalShipCells(deckNumber, placementCells, x, y);
            } else {
                x = x - deckNumber + 1;
                setHorizontalShipCells(deckNumber, placementCells, x, y);
            }
        }

        return converterService.toCells(placementCells);
    }

    private void setHorizontalShipCells(int deckNumber, Cell[][] placementCells, int x, int y) {

        int deckCounter = 1;
        for (int i = x; i < x + deckNumber; i++, deckCounter++) {
            setShipCell(placementCells[y][i], deckNumber);
            setHorizontalUnavailableCells(deckNumber, deckCounter, i, placementCells, y);
        }
    }

    private void setHorizontalUnavailableCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int y) {
        if (deckCounter == 1) {
            if (i > 0) {
                setNearShipCell(placementCells[y][i - 1]);

                if (y > 0) {
                    setNearShipCell(placementCells[y - 1][i - 1]);
                }
                if (y < 9) {
                    setNearShipCell(placementCells[y + 1][i - 1]);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (y > 0) {
                setNearShipCell(placementCells[y - 1][i]);
            }
            if (y < 9) {
                setNearShipCell(placementCells[y + 1][i]);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                setNearShipCell(placementCells[y][i + 1]);
                if (y > 0) {
                    setNearShipCell(placementCells[y - 1][i + 1]);
                }
                if (y < 9) {
                    setNearShipCell(placementCells[y + 1][i + 1]);
                }
            }
        }
    }

    private void setVerticalShipCells(int deckNumber, Cell[][] placementCells, int x, int y) {
        int deckCounter = 1;
        for (int i = y; i < y + deckNumber; i++, deckCounter++) {
            setShipCell(placementCells[i][x], deckNumber);
            setVerticalUnavailableCells(deckNumber, deckCounter, i, placementCells, x);
        }
    }

    private void setVerticalUnavailableCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int x) {
        if (deckCounter == 1) {
            if (i > 0) {
                setNearShipCell(placementCells[i - 1][x]);
                if (x > 0) {
                    setNearShipCell(placementCells[i - 1][x - 1]);
                }
                if (x < 9) {
                    setNearShipCell(placementCells[i - 1][x + 1]);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (x > 0) {
                setNearShipCell(placementCells[i][x - 1]);
            }
            if (x < 9) {
                setNearShipCell(placementCells[i][x + 1]);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                setNearShipCell(placementCells[i + 1][x]);
                if (x > 0) {
                    setNearShipCell(placementCells[i + 1][x - 1]);
                }
                if (x < 9) {
                    setNearShipCell(placementCells[i + 1][x + 1]);
                }
            }
        }
    }

    private void setShipCell(Cell cell, int deckNumber) {
        cell.setPictureAddress(ColorCellType.GREEN_CELL.colorID);
        cell.setShipSize(deckNumber);
        cell.setCellState(CellState.OCCUPIED);
    }

    private void setNearShipCell(Cell cell) {
        cell.increaseNearShipCounter();
        cell.setCellState(CellState.UNAVAILABLE);
        cell.setPictureAddress(ColorCellType.RED_CELL.colorID);
    }

    private boolean isCellsAvailable(int deckNumber, boolean orientation, Cell[][] placementCells, int x, int y) {
        boolean cellsComparator;
        Cell tempCell;
        if (orientation) {
            if (y + deckNumber - 1 < 10) {
                tempCell = placementCells[y + deckNumber - 1][x];
                cellsComparator = tempCell.getCellState() == CellState.EMPTY;
                return cellsComparator;
            } else {
                tempCell = placementCells[y - deckNumber + 1][x];
                cellsComparator = tempCell.getCellState() == CellState.EMPTY;
                return cellsComparator;
            }
        } else {
            if (x + deckNumber - 1 < 10) {
                tempCell = placementCells[y][x + deckNumber - 1];
                cellsComparator = tempCell.getCellState() == CellState.EMPTY;
                return cellsComparator;
            } else {
                tempCell = placementCells[y][x - deckNumber + 1];
                cellsComparator = tempCell.getCellState() == CellState.EMPTY;
                return cellsComparator;
            }
        }
    }

    public Cell[] removeShip(Cell[] cells, int position) {
        Cell[][] placementCells = converterService.toPlacementCells(cells);
        int x = converterService.setX(position);
        int y = converterService.setY(position);
        int deckNumber = cells[position].getShipSize();

        if (shipService.isShipOrientation(placementCells, x, y)) {
            while (y > 1 && placementCells[y][x].getShipSize() == placementCells[y - 1][x].getShipSize()) {
                y -= 1;

            }
            removeVerticalShipCells(deckNumber, placementCells, x, y);
        } else {
            while (x > 1 && placementCells[y][x].getShipSize() == placementCells[y][x - 1].getShipSize()) {
                x -= 1;
            }
            removeHorizontalShipCells(deckNumber, placementCells, x, y);
        }
        return converterService.toCells(placementCells);
    }

    private void removeHorizontalShipCells(int deckNumber, Cell[][] placementCells, int x, int y) {
        int deckCounter = 1;
        for (int i = x; i < x + deckNumber; i++, deckCounter++) {
            removeShipCell(placementCells[y][i]);
            removeHorizontalUnavailableCells(deckNumber, deckCounter, i, placementCells, y);
        }
    }

    private void removeHorizontalUnavailableCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int y) {
        if (deckCounter == 1) {
            if (i > 0) {
                removeNearShipCell(placementCells[y][i - 1]);

                if (y > 0) {
                    removeNearShipCell(placementCells[y - 1][i - 1]);
                }
                if (y < 9) {
                    removeNearShipCell(placementCells[y + 1][i - 1]);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (y > 0) {
                removeNearShipCell(placementCells[y - 1][i]);
            }
            if (y < 9) {
                removeNearShipCell(placementCells[y + 1][i]);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                removeNearShipCell(placementCells[y][i + 1]);
                if (y > 0) {
                    removeNearShipCell(placementCells[y - 1][i + 1]);
                }
                if (y < 9) {
                    removeNearShipCell(placementCells[y + 1][i + 1]);
                }
            }
        }
    }

    private void removeVerticalShipCells(int deckNumber, Cell[][] placementCells, int x, int y) {
        int deckCounter = 1;
        for (int i = y; i < y + deckNumber; i++, deckCounter++) {
            removeShipCell(placementCells[i][x]);
            removeVerticalUnavailableCells(deckNumber, deckCounter, i, placementCells, x);
        }
    }

    private void removeVerticalUnavailableCells(int deckNumber, int deckCounter, int i, Cell[][] placementCells, int x) {
        if (deckCounter == 1) {
            if (i > 0) {
                removeNearShipCell(placementCells[i - 1][x]);

                if (x > 0) {
                    removeNearShipCell(placementCells[i - 1][x - 1]);
                }
                if (x < 9) {
                    removeNearShipCell(placementCells[i - 1][x + 1]);
                }
            }
        }
        if (deckCounter <= deckNumber) {
            if (x > 0) {
                removeNearShipCell(placementCells[i][x - 1]);
            }
            if (x < 9) {
                removeNearShipCell(placementCells[i][x + 1]);
            }
        }
        if (deckCounter == deckNumber) {
            if (i < 9) {
                removeNearShipCell(placementCells[i + 1][x]);
                if (x > 0) {
                    removeNearShipCell(placementCells[i + 1][x - 1]);
                }
                if (x < 9) {
                    removeNearShipCell(placementCells[i + 1][x + 1]);
                }
            }
        }
    }

    private void removeShipCell(Cell cell) {
        cell.setPictureAddress(ColorCellType.WHITE_CELL.colorID);
        cell.setShipSize(0);
        cell.setCellState(CellState.EMPTY);
    }

    private void removeNearShipCell(Cell cell) {
        cell.decreaseNearShipCounter();
        if (cell.getNearShipCounter() < 1) {
            cell.setCellState(CellState.EMPTY);
            cell.setPictureAddress(ColorCellType.WHITE_CELL.colorID);
        }
    }

    public boolean isRemoveShipAllowed(Cell cell) {
        return cell.getCellState() == CellState.OCCUPIED;
    }
}
