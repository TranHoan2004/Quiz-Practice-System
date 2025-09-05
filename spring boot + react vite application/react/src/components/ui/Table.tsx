import {
    Table,
    TableHeader,
    TableColumn,
    TableBody,
    TableRow,
    TableCell,
    Pagination,
    Spinner,
} from "@heroui/react";
import {ReactNode} from "react";

interface DataTableProps<T> {
    columns: { name: string; uid: string; align?: "start" | "center" | "end" }[];
    items: T[];
    page: number;
    totalPages: number;
    onPageChange: (page: number) => void;
    renderCell: (item: T, columnKey: string) => ReactNode;
    emptyText?: string;
    loading?: boolean;
}

export function DataTable<T extends { id: string }>({
                                                        columns,
                                                        items,
                                                        page,
                                                        totalPages,
                                                        onPageChange,
                                                        renderCell,
                                                        emptyText = "No data.",
                                                        loading = false,
                                                    }: DataTableProps<T>) {
    return (
        <Table
            aria-label="Reusable data table"
            bottomContent={
                <div className="flex w-full justify-center">
                    {!loading && totalPages >= 1 && (
                        <Pagination
                            isCompact
                            showControls
                            showShadow
                            classNames={{
                                cursor: "bg-gradient-to-r from-blue-600 to-purple-600 text-white",
                            }}
                            page={page}
                            total={totalPages}
                            onChange={onPageChange}
                        />
                    )}
                </div>
            }
            classNames={{
                wrapper: "min-h-[222px]",
            }}
        >
            <TableHeader columns={columns}>
                {(column) => (
                    <TableColumn
                        key={column.uid}
                        align={column.align ?? (column.uid === "actions" ? "center" : "start")}
                    >
                        {column.name}
                    </TableColumn>
                )}
            </TableHeader>

            <TableBody
                emptyContent={
                    loading ? <Spinner label="Loading..." /> : emptyText
                }
                items={loading ? [] : items}
            >
                {(item) => (
                    <TableRow key={item.id}>
                        {columns.map((col) => (
                            <TableCell key={col.uid}>
                                {renderCell(item, col.uid)}
                            </TableCell>
                        ))}
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}
