
import { Button } from "@heroui/button";
import {
    Chip,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader
} from "@heroui/react";
import React, { useState } from "react";

import { EyeIcon } from "@/components/icons";

interface UserViewModalProps {
    user: {
        id: string;
        fullName: string;
        email: string;
        status: string;
        gender: string;
        role: string;
        createdDate: string;
        phoneNumber?: string;
    } | null;
}

const UserViewModal: React.FC<UserViewModalProps> = ({ user }) => {
    const [open, setOpen] = useState(false);

    if (!user) return null;

    return (
        <>
            <Button
                isIconOnly
                variant="bordered"
                onPress={() => setOpen(true)}
            >
                <EyeIcon />
            </Button>
            <Modal isOpen={open} onOpenChange={setOpen}>
                <ModalContent>
                    {(close) => (
                        <>
                            <ModalHeader>View User Details</ModalHeader>
                            <ModalBody>
                                <div className="mb-2">
                                    <strong>ID:</strong> {user.id}
                                </div>

                                <div className="mb-2">
                                    <strong>Name:</strong> {user.fullName}
                                </div>

                                <div className="mb-2">
                                    <strong>Email:</strong> {user.email}
                                </div>

                                <div className="mb-2">
                                    <strong>Role:</strong>
                                    <span className="px-2 py-1 ml-2 rounded-lg text-sm font-medium bg-gradient-to-r from-blue-100
                                        to-purple-100 text-blue-700 dark:from-gray-700 dark:to-gray-600 dark:text-gray-200">
                                        {user.role}
                                    </span>
                                </div>

                                {user.phoneNumber && (
                                    <div className="mb-2">
                                        <strong>Phone Number:</strong> {user.phoneNumber}
                                    </div>
                                )}

                                <div className="mb-2">
                                    <strong>Gender:</strong> {user.gender ? user.gender : 'N/A'}
                                </div>

                                <div className="mb-2">
                                    <strong>Created Date:</strong> {user.createdDate}
                                </div>

                                <div className="mb-2">
                                    <strong>Status:</strong>
                                    <Chip
                                        className="px-3 py-1 ml-2 rounded-full text-sm font-medium text-white text-center"
                                        color={user.status ? "success" : "danger"}
                                        size="sm"
                                    >
                                        {user.status ? "Active" : "Inactive"}
                                    </Chip>
                                </div>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="default" variant="flat" onPress={close}>
                                    Close
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    );
};

export default UserViewModal;
