export class PaginationModel {
    content: any;
    totalPages: any;
    totalElements: number;
    pageNumber: number;
    pageSize: number;
    pageLast: boolean;
    pageFirst: boolean;
    startNumberItem: number;
    endNumberItem: number;
    pageNumbers: number[] = [];


    constructor(data: any) {
        this.content = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.pageNumber = data.pageNumber;
        this.pageSize = data.pageSize;
        this.startNumberItem = data.startNumberItem;
        this.endNumberItem = data.endNumberItem;
        this.pageLast = data.pageLast;
        this.pageFirst = data.pageFirst;
        this.calculatePageNumbers();
    }

    calculatePageNumbers() {
        const totalPages = this.totalPages;
        const currentPage = this.pageNumber;

        if (totalPages > 0) {
            let start = Math.max(1, currentPage - 2);
            let end = Math.min(currentPage + 2, totalPages);

            if (totalPages > 4) {
                if (end === totalPages) start = end - 4;
                else if (start === 1) end = start + 4;
            }

            this.pageNumbers = Array.from({ length: end - start + 1 }, (_, index) => start + index);
        }
    }
}