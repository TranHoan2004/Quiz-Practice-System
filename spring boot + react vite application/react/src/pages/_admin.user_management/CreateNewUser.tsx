import { useState } from 'react';
import { z } from "zod";
import { Button } from "@heroui/button";
import { Plus } from "lucide-react";
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
import { Input } from "@heroui/input";

import { Role } from "@/constants/general.constant.ts";
import { createUser } from "@/services/user.service.ts";
import { useAuth } from '@/hooks/useAuth';

interface CreateNewUserProps {
    roles: Role[];
}

const CreateNewUser = ({ roles }: CreateNewUserProps) => {
    const { isOpen, onClose, onOpen, onOpenChange } = useDisclosure();
    const [form, setForm] = useState({
        name: "",
        email: "",
        role: "",
        phoneNumber: "",
    });
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const { user } = useAuth();

    const userSchema = z.object({
        name: z.string().min(1, "Name is required"),
        email: z.string().email("Invalid email").min(1, "Email is required"),
        role: z.string().min(1, "Role is required"),
        phoneNumber: z.string().regex(/^0\d{10}$/, "Phone number must start with 0 and be exactly 11 digits"),
    });

    const handleChange = (field: string, value: string) => {
        setForm((prev) => ({ ...prev, [field]: value }));
    };

    const handleSave = () => {
        const result = userSchema.safeParse(form);

        if (!result.success) {
            const fieldErrors: { [key: string]: string } = {};

            result.error.issues.forEach((err) => {
                const path = err.path[0];

                if (typeof path === 'string') {
                    fieldErrors[path] = err.message;
                }
            });
            setErrors(fieldErrors);

            return;
        }
        setErrors({});
        (async () => {
            if (user) {
                await createUser(user.accessToken, result.data);
                onClose();
            }
        })();
    };

    return (
        <>
            <Button
                className="max-w-1/3 bg-gradient-to-r from-blue-600 to-purple-600 text-white font-semibold px-4 py-2 rounded-xl shadow-md hover:opacity-90 transition"
                onPress={onOpen}
            >
                <Plus className="w-4 h-4 mr-2" />
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
                                    errorMessage={errors.name}
                                    label="Full Name"
                                    placeholder="Enter full name"
                                    value={form.name}
                                    onChange={(e) => handleChange("name", e.target.value)}
                                />
                                <Input
                                    isRequired
                                    errorMessage={errors.email}
                                    label="Email"
                                    placeholder="Enter email"
                                    type="email"
                                    value={form.email}
                                    onChange={(e) => handleChange("email", e.target.value)}
                                />
                                <Select
                                    isRequired
                                    errorMessage={errors.role}
                                    label="Role"
                                    placeholder="Select role"
                                    selectedKeys={form.role ? [form.role] : []}
                                    onChange={(e) => handleChange("role", e.target.value)}
                                >
                                    {roles.map((role) => (
                                        <SelectItem key={role}>{role}</SelectItem>
                                    ))}
                                </Select>
                                <Input
                                    isRequired
                                    errorMessage={errors.phoneNumber}
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
