package com.binary.webrtc;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yaoguoju on 18-1-24.
 */

public class ImRTCClient implements AppRTCClient {
    private static final String TAG = "ImRTCClient";

    private enum ConnectionState { NEW, CONNECTED, CLOSED, ERROR }

    private ConnectionState roomState;
    private SignalingEvents events;
    private ImInterface     imInterface;
    private final ExecutorService executor;
    private ImRTCSession imRTCSession;

    public static ImRTCClient imRTCClient;

    public ImRTCClient() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static ImRTCClient getImRTCClient() {
        if(imRTCClient == null) {
            synchronized (ImRTCClient.class) {
                if(imRTCClient == null) {
                    imRTCClient = new ImRTCClient();
                }
            }
        }
        return imRTCClient;
    }

    public void setSignalingEvents(SignalingEvents events) {
        this.events = events;
        roomState = ConnectionState.NEW;
    }

    public interface ImInterface {
        void sendMessage(String msg);
    }

    public void setImInterface(ImInterface imInterface) {
        this.imInterface = imInterface;
    }


    @Override
    public void sendOfferSdp(SessionDescription sdp) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (roomState != ConnectionState.CONNECTED) {
                    reportError("Sending offer SDP in non connected state.");
                    return;
                }
                JSONObject json = new JSONObject();
                jsonPut(json, "sdp", sdp.description);
                jsonPut(json, "type", "offer");
                sendMessage(json.toString());
            }
        });
    }

    // --------------------------------------------------------------------
    // Helper functions.
    private void reportError(final String errorMessage) {
        Log.e(TAG, errorMessage);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (roomState != ConnectionState.ERROR) {
                    roomState = ConnectionState.ERROR;
                    events.onChannelError(errorMessage);
                }
            }
        });
    }

    private void sendMessage(final String message) {
        if(imInterface != null) {
            imInterface.sendMessage(message);
        }
    }

    // Put a |key|->|value| mapping in |json|.
    private static void jsonPut(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sendAnswerSdp(SessionDescription sdp) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                jsonPut(json, "sdp", sdp.description);
                jsonPut(json, "type", "answer");
                sendMessage(json.toString());
            }
        });
    }

    @Override
    public void sendLocalIceCandidate(IceCandidate candidate) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                jsonPut(json, "type", "candidate");
                jsonPut(json, "label", candidate.sdpMLineIndex);
                jsonPut(json, "id", candidate.sdpMid);
                jsonPut(json, "candidate", candidate.sdp);

                if (roomState != ConnectionState.CONNECTED) {
                    reportError("Sending ICE candidate in non connected state.");
                    return;
                }
                sendMessage(json.toString());
            }
        });
    }

    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] candidates) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                jsonPut(json, "type", "remove-candidates");
                JSONArray jsonArray = new JSONArray();
                for (final IceCandidate candidate : candidates) {
                    jsonArray.put(toJsonCandidate(candidate));
                }
                jsonPut(json, "candidates", jsonArray);

                if (roomState != ConnectionState.CONNECTED) {
                    reportError("Sending ICE candidate removals in non connected state.");
                    return;
                }
                sendMessage(json.toString());
            }
        });
    }

    private static JSONObject toJsonCandidate(final IceCandidate candidate) {
        JSONObject json = new JSONObject();
        jsonPut(json, "label", candidate.sdpMLineIndex);
        jsonPut(json, "id", candidate.sdpMid);
        jsonPut(json, "candidate", candidate.sdp);
        return json;
    }

    // Converts a JSON candidate to a Java object.
    private static IceCandidate toJavaCandidate(JSONObject json) throws JSONException {
        return new IceCandidate(
                json.getString("id"), json.getInt("label"), json.getString("candidate"));
    }


    public void onImMessageReceived(String msg) {
        Log.d(TAG,"msg "+msg);
        try {
            JSONObject json = new JSONObject(msg);
            String type = json.optString("type");
            if (type.equals("candidate")) {
                events.onRemoteIceCandidate(toJavaCandidate(json));
            } else if (type.equals("remove-candidates")) {
                JSONArray candidateArray = json.getJSONArray("candidates");
                IceCandidate[] candidates = new IceCandidate[candidateArray.length()];
                for (int i = 0; i < candidateArray.length(); ++i) {
                    candidates[i] = toJavaCandidate(candidateArray.getJSONObject(i));
                }
                events.onRemoteIceCandidatesRemoved(candidates);
            } else if (type.equals("answer")) {
                SessionDescription sdp = new SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type), json.getString("sdp"));
                events.onRemoteDescription(sdp);
            } else if (type.equals("offer")) {
                SessionDescription sdp = new SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type), json.getString("sdp"));
                List<PeerConnection.IceServer> iceServerList = new ArrayList<>();

                List<String> urls = new ArrayList<>();
                urls.add("turn:61.152.175.119:3478");
                PeerConnection.IceServer.Builder builder = PeerConnection.IceServer.builder(urls);
                builder.setUsername("test");
                builder.setPassword("test");
                iceServerList.add(builder.createIceServer());

                SignalingParameters parameters = new SignalingParameters(
                        // Ice servers are not needed for direct connections.
                        iceServerList,
                        false, // This code will only be run on the client side. So, we are not the initiator.
                        null, // clientId
                        null, // wssUrl
                        null, // wssPostUrl
                        sdp, // offerSdp
                        null // iceCandidates
                );
                roomState = ConnectionState.CONNECTED;
                events.onConnectedToRoom(parameters);
            } else {
                reportError("Unexpected TCP message: " + msg);
            }
        } catch (JSONException e) {
            reportError("TCP message JSON parsing error: " + e.toString());
        }
    }


    @Override
    public void addImRTCSession(ImRTCSession session) {
        this.imRTCSession = session;
    }

    @Override
    public ImRTCSession getImRTCSession() {
        return this.imRTCSession;
    }

    @Override
    public void connectToRoom(RoomConnectionParameters connectionParameters) {

    }

    @Override
    public void disconnectFromRoom() {

    }
}
