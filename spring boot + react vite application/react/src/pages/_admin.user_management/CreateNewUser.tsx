import {useState} from 'react';
import {Button} from "@heroui/button";
import {Plus} from "lucide-react";
import {
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Select,
    SelectItem,
    useDisclosure
} from "@heroui/react";
import {Input} from "@heroui/input";

import {Role} from "@/constants/general.constant.ts";
import {createUser} from "@/services/user.service.ts";

interface CreateNewUserProps {
    roles: Role[];
}

const CreateNewUser = ({roles}: CreateNewUserProps) => {
    const {isOpen, onClose, onOpen, onOpenChange} = useDisclosure();
    const [form, setForm] = useState({
        name: "",
        email: "",
        role: "",
        phoneNumber: "",
    });
    const handleChange = (field: string, value: string) => {
        setForm((prev) => ({...prev, [field]: value}));
    };

    const handleSave = () => {
        (async () => {
            await createUser(form);
            onClose();
        })();
    };

    return (
        <>
            <Button
                className="max-w-1/3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold px-4 py-2 rounded-xl shadow-md hover:opacity-90 transition"
                onPress={onOpen}
            >
                <Plus className="w-4 h-4 mr-2"/>
                Add User
            </Button>

            <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">
                                Create New User
                            </ModalHeader>
                            <ModalBody>
                                <Input
                                    isRequired
                                    label="Full Name"
                                    placeholder="Enter full name"
                                    value={form.name}
                                    onChange={(e) => handleChange("name", e.target.value)}
                                />
                                <Input
                                    isRequired
                                    label="Email"
                                    placeholder="Enter email"
                                    type="email"
                                    value={form.email}
                                    onChange={(e) => handleChange("email", e.target.value)}
                                />
                                <Select
                                    isRequired
                                    label="Role"
                                    placeholder="Select role"
                                    selectedKeys={form.role ? [form.role] : []}
                                    onChange={(e) => handleChange("role", e.target.value)}
                                >
                                    {roles.map((role) => (
                                        <>
                                            <SelectItem key={role}>{role}</SelectItem>
                                        </>
                                    ))}
                                </Select>
                                <Input
                                    isRequired
                                    label="Phone number"
                                    placeholder="Enter phone number"
                                    type="number"
                                    value={form.phoneNumber}
                                    onChange={(e) => handleChange("phoneNumber", e.target.value)}
                                />
                            </ModalBody>
                            <ModalFooter>
                                <Button variant="flat" onPress={onClose}>
                                    Cancel
                                </Button>
                                <Button
                                    className="bg-gradient-to-r from-blue-600 to-purple-600 text-white"
                                    onPress={handleSave}
                                >
                                    Save
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    );
};

export default CreateNewUser;
