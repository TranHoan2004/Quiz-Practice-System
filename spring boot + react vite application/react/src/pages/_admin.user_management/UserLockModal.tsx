import { Button } from "@heroui/button";
import {
    addToast,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader
} from "@heroui/react";
import { useState } from "react";

import { lockUser } from "@/services/user.service";
import { LockIcon } from "@/components/icons";
import { useAuth } from "@/hooks/useAuth";

interface UserLockModalProps {
    userDetails: {
        id: string;
        name: string;
        email: string;
        status?: boolean;
        phoneNumber?: string;
    };
}

const UserLockModal = ({ userDetails }: UserLockModalProps) => {
    const [open, setOpen] = useState(false);
    const { user } = useAuth();

    if (!userDetails) return null;

    const handleLock = () => {
        if (userDetails.status == false) {
            addToast({
                title: "User Inactive",
                description: "User account is already locked.",
                color: "warning",
                closeIcon: true,
            })

            return;
        }
        if (user) {
            lockUser(userDetails.id, user.accessToken);
        setOpen(false);
        }
    };

    return (
        <>
            <Button
                isIconOnly
                className="rounded-full border hover:border-red-400"
                variant="bordered"
                onPress={() => setOpen(true)}
            >
                <LockIcon className="w-4 h-4 text-red-500" />
            </Button>
            <Modal isOpen={open} onOpenChange={setOpen}>
                <ModalContent>
                    {(close) => (
                        <>
                            <ModalHeader>Lock User Account</ModalHeader>
                            <ModalBody>
                                <div className="mb-2">
                                    Are you sure you want to lock the account for <strong>{userDetails.name}</strong> ({userDetails.email})?
                                </div>
                            </ModalBody>
                            <ModalFooter>
                                <Button color="default" variant="flat" onPress={close}>
                                    Cancel
                                </Button>
                                <Button color="danger" variant="solid" onPress={handleLock}>
                                    Lock
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    );
};

export default UserLockModal;
