import {Kbd} from "@heroui/kbd";
import {Input} from "@heroui/input";

import {SearchIcon} from "@/components/icons.tsx";

interface SearchBarProps {
    className?: string,
    value: string,
    onChange: (value: string) => void,
    placeholder?: string,
    type: string,
}

const SearchBar = ({
                       className,
                       value,
                       onChange,
                       placeholder,
                       type
                   }: SearchBarProps) => {
    return (
        <Input
            aria-label="Search"
            className={className}
            classNames={{
                inputWrapper: "bg-default-100",
                input: "text-sm",
            }}
            endContent={
                <Kbd className="hidden lg:inline-block" keys={["command"]}>
                    K
                </Kbd>
            }
            labelPlacement="outside"
            placeholder={placeholder}
            startContent={
                <SearchIcon className="text-base text-default-400 pointer-events-none flex-shrink-0"/>
            }
            type={type}
            value={value}
            onChange={(e) => onChange(e.target.value)}
        />
    );
};

export default SearchBar;
