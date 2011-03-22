package com.androidplot.ui.layout;

import android.graphics.RectF;

import java.util.Iterator;

public class FixedTableModel extends TableModel {
    private float cellWidth;
    private float cellHeight;
    protected FixedTableModel(float cellWidth, float cellHeight, TableOrder order) {
        super(order);
        setCellWidth(cellWidth);
        setCellHeight(cellHeight);
    }

    @Override
    public Iterator<RectF> getIterator(RectF tableRect, int totalElements) {
        return new FixedTableModelIterator(this, tableRect, totalElements);
    }

    public float getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(float cellWidth) {
        this.cellWidth = cellWidth;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    private class FixedTableModelIterator implements Iterator<RectF> {

        private FixedTableModel model;
        private RectF tableRect;
        private RectF lastRect;
        private int numElements;
        private int lastElement;
        protected FixedTableModelIterator(FixedTableModel model, RectF tableRect, int numElements) {
            this.model = model;
            this.tableRect = tableRect;
            this.numElements = numElements;
            lastRect = new RectF(
                    tableRect.left,
                    tableRect.top,
                    tableRect.left + model.getCellWidth(),
                    tableRect.top + model.getCellHeight());
        }

        @Override
        public boolean hasNext() {
            // was this the last element or is there no room in either axis for another cell?
            if(lastElement >= numElements || (isColumnFinished() && isRowFinished())) {
                return false;
            } else {
                return true;
            }
        }

        private boolean isColumnFinished() {
            if(lastRect.bottom + model.getCellHeight() > tableRect.height()) {
                return true;
            }
            return false;
        }

        private boolean isRowFinished() {
            if(lastRect.right + model.getCellWidth() > tableRect.width() ) {
                return true;
            }
            return false;
        }

        @Override
        public RectF next() {
            try {
                if (lastElement == 0) {
                    return lastRect;
                }

                if (lastElement >= numElements) {
                    throw new IndexOutOfBoundsException();
                }
                switch (model.getOrder()) {
                    case ROW_MAJOR:
                        if (isColumnFinished()) {
                            moveOverAndUp();
                        } else {
                            moveDown();
                        }
                        break;
                    case COLUMN_MAJOR:
                        if (isRowFinished()) {
                            moveDownAndBack();
                        } else {
                            moveOver();
                        }
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                return lastRect;
            } finally {
                lastElement++;
            }
        }

        private void moveDownAndBack() {
            //RectF rect = new RectF(lastRect);
            lastRect.offsetTo(tableRect.left, lastRect.bottom);
            //return rect;
        }

        private void moveOverAndUp() {
            //RectF rect = new RectF(lastRect);
            lastRect.offsetTo(lastRect.right, tableRect.top);
            //return rect;
        }

        private void moveOver() {
            //RectF rect = new RectF(lastRect);
            lastRect.offsetTo(lastRect.right, lastRect.top);
            //return rect;
        }

        private void moveDown() {
            //RectF rect = new RectF(lastRect);
            lastRect.offsetTo(lastRect.left, lastRect.bottom);
            //return rect;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}