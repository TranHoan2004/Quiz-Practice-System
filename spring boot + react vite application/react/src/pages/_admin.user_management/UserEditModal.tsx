import { Button } from "@heroui/button";
import { Input } from "@heroui/input";
import {
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Select,
    SelectItem
} from "@heroui/react";
import { useState, useEffect } from "react";
import { z } from "zod";

import { EditIcon } from "@/components/icons";
import { STATUS } from "@/constants/general.constant";
import { editUser } from "@/services/user.service";

interface UserEditModalProps {
    user: {
        id: string;
        fullName: string;
        email: string;
        status?: string;
        phoneNumber?: string;
    } | null;
}

const status = [STATUS.ACTIVE, STATUS.INACTIVE];

const UserEditModal = ({ user }: UserEditModalProps) => {
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const userEditSchema = z.object({
        id: z.string().length(36, "ID must be exactly 36 characters"),
        fullName: z.string().min(1, "Full Name is required"),
        email: z.string().email("Invalid email").min(1, "Email is required"),
        status: z.enum(["Active", "Inactive"]),
        phoneNumber: z.string()
            .regex(/^0\d{10}$/, "Phone number must start with 0 and be exactly 11 digits")
            .optional(),
    });
    const [open, setOpen] = useState(false);
    const [form, setForm] = useState({
        id: user?.id || '',
        fullName: user?.fullName || '',
        email: user?.email || '',
        status: user?.status || '',
        phoneNumber: user?.phoneNumber || '',
    });

    useEffect(() => {
        setForm({
            id: user?.id || '',
            fullName: user?.fullName || '',
            email: user?.email || '',
            status: user?.status || '',
            phoneNumber: user?.phoneNumber || '',
        });
    }, [user]);

    if (!user) return null;

    const handleChange = (field: string, value: string) => {
        setForm({ ...form, [field]: value });
    };

    const handleSave = () => {
        const result = userEditSchema.safeParse(form);

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
        editUser(result.data);
        setOpen(false);
    };

    return (
        <>
            <Button
                isIconOnly
                variant="bordered"
                onPress={() => setOpen(true)}
            >
                <EditIcon />
            </Button>
            <Modal isOpen={open} onOpenChange={setOpen}>
                <ModalContent>
                    {(close) => (
                        <>
                            <ModalHeader>Edit User Details</ModalHeader>
                            <ModalBody>
                                <Input
                                    isRequired
                                    errorMessage={errors.fullName}
                                    label="Full Name"
                                    placeholder="Enter full name"
                                    value={form.fullName}
                                    onChange={e => handleChange('fullName', e.target.value)}
                                />
                                <Input
                                    isRequired
                                    errorMessage={errors.email}
                                    label="Email"
                                    placeholder="Enter email"
                                    type="email"
                                    value={form.email}
                                    onChange={e => handleChange('email', e.target.value)}
                                />
                                <Input
                                    isRequired
                                    errorMessage={errors.phoneNumber}
                                    label="Phone Number"
                                    placeholder="Enter phone number"
                                    type="number"
                                    value={form.phoneNumber}
                                    onChange={e => handleChange('phoneNumber', e.target.value)}
                                />
                                <Select
                                    isRequired
                                    errorMessage={errors.status}
                                    label="Status"
                                    placeholder="Select status"
                                    selectedKeys={form.status ? [form.status] : []}
                                    onChange={e => handleChange('status', e.target.value)}
                                >
                                    {status.map(opt => (
                                        <SelectItem key={opt}>
                                            {opt}
                                        </SelectItem>
                                    ))}
                                </Select>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="default" variant="flat" onPress={close}>
                                    Cancel
                                </Button>
                                <Button className="bg-gradient-to-r from-blue-600 to-purple-600 text-white" variant="solid" onPress={handleSave}>
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

export default UserEditModal;
