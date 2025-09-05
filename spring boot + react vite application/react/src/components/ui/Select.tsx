import {Select, SelectItem} from "@heroui/react";
import React from "react";

type DataSelectProps<T extends string | number | undefined> = {
    selectItems: T[];
    className?: string;
    data: T;
    placeholder: string;
    setData: React.Dispatch<React.SetStateAction<T>>;
    props?: any
}

function DataSelect<T extends string | number | undefined>({
                                                               className,
                                                               selectItems,
                                                               data,
                                                               placeholder,
                                                               setData,
                                                               props
                                                           }: DataSelectProps<T>) {
    return (
        <Select
            className={className}
            placeholder={placeholder}
            selectedKeys={data ?
                // Ép kiểu dữ liệu về string trong trường hợp nó là number
                [String(data)] : []}
            onSelectionChange={(keys) => {
                const value = Array.from(keys)[0] as string;

               // Ép ngược về dữ liệu gốc
                setData((typeof data === "number" ? Number(value) : value) as T);
            }}
            {...props}
        >
            {selectItems.map((item: any) => (
                <SelectItem key={item} className="capitalize" textValue={item}>
                    {item}
                </SelectItem>
            ))}
        </Select>
    );
}

export default DataSelect;
