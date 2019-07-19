package cn.haoxy.zk.api.utils;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class AclUtils {

	//加密 ,将明文传入,加密之后返回
	public static String getDigestUserPwd(String id) throws Exception {
		return DigestAuthenticationProvider.generateDigest(id);
	}
	
	public static void main(String[] args) throws Exception {
		String id = "haoxy:haoxy";
		String idDigested = getDigestUserPwd(id);
		System.out.println(idDigested);//haoxy:Sm6Y7C7Lz+Zw3Dg5QPqU15Vy1Vg=
	}
}
