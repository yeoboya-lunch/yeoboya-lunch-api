package com.yeoboya.lunch.api.v1.member.reqeust;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberInfoEditor {

    private String bio;
    private String nickName;
    private String phoneNumber;

    public MemberInfoEditor(String bio, String nickName, String phoneNumber) {
        this.bio = bio;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public static MemberInfoEditorBuilder builder() {
        return new MemberInfoEditorBuilder();
    }

    public static class MemberInfoEditorBuilder {
        private String bio;
        private String nickName;
        private String phoneNumber;


        public MemberInfoEditorBuilder bio(final String bio) {
            if (bio != null) {
                this.bio = bio;
            };
            return this;
        }

        public MemberInfoEditorBuilder nickName(final String nickName) {
            if (nickName != null) {
                this.nickName = nickName;
            }
            return this;
        }

        public MemberInfoEditorBuilder phoneNumber(final String phoneNumber) {
            if (phoneNumber != null) {
                this.phoneNumber = phoneNumber;
            }
            return this;
        }

        public MemberInfoEditor build() {
            return new MemberInfoEditor(this.bio, this.nickName, this.phoneNumber);
        }

        public String toString() {
            return "MemberInfoEdit.MemberInfoEditBuilder(bio=" + this.bio + ", nickName=" + this.nickName + ", phoneNumber=" + this.phoneNumber + ")";
        }
    }


}
